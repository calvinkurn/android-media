package com.tokopedia.tradein.view.viewcontrollers

import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.tradein.R
import com.tokopedia.tradein.Utils
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption.ScheduleDate
import com.tokopedia.tradein.viewmodel.MoneyInCheckoutViewModel
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class MoneyInCheckoutActivity : BaseTradeInActivity() {
    private lateinit var moneyInCheckoutViewModel : MoneyInCheckoutViewModel
    companion object {
        const val MONEY_IN_DEFAULT_ADDRESS = "MONEY_IN_DEFAULT_ADDRESS"
        const val MONEY_IN_NEW_ADDRESS = "MONEY_IN_NEW_ADDRESS"
        const val MONEY_IN_REQUEST_CHECKOUT = 8952
        const val MONEY_IN_ORDER_VALUE = "MONEY_IN_PRICE"
        const val MONEY_IN_HARDWARE_ID ="HARDWARE_ID"
    }

    override fun initView() {
        moneyInCheckoutViewModel.getPickupScheduleOption(getMeGQlString(R.raw.gql_get_pickup_schedule_option))
        setObservers()
    }

    private fun setObservers() {
        moneyInCheckoutViewModel.getMoneyInAddressLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    if (!it.data.data.isNullOrEmpty())
                        setAddressView(it.data.data?.get(0))
                }
                is Fail -> {
                }
            }
        })
        moneyInCheckoutViewModel.getPickupScheduleOptionLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    if (!it.data.scheduleDate.isNullOrEmpty())
                        setScheduleBottomSheet(it.data.scheduleDate!!)
                }
                is Fail -> {
                }
            }
        })
        moneyInCheckoutViewModel.getCourierRatesLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    setCourierRatesBottomSheet(it.data)
                }
                is Fail -> {
                }
            }
        })
        moneyInCheckoutViewModel.getCheckoutDataLiveData().observe(this, Observer {
            when (it) {
                is Success -> {

                }
                is Fail -> {
                }
            }
        })
    }

    private fun setCourierRatesBottomSheet(data: RatesV4.Data) {
        data.error
    }

    private fun setScheduleBottomSheet(scheduleDate: List<ScheduleDate?>) {

    }

    private fun setAddressView(recipientAddress: KeroGetAddress.Data?) {
        val tvAddressStatus = findViewById<Typography>(R.id.tv_address_status) as Typography
        val tvAddressName = findViewById<Typography>(R.id.tv_address_name) as Typography
        val tvRecipientName = findViewById<Typography>(R.id.tv_recipient_name) as Typography
        val tvRecipientAddress = findViewById<Typography>(R.id.tv_recipient_address) as Typography
        val tvRecipientPhone = findViewById<Typography>(R.id.tv_recipient_phone) as Typography
        val tvChangeRecipientAddress = findViewById<Typography>(R.id.tv_change_recipient_address) as Typography

        if (recipientAddress?.status == 2) {
            tvAddressStatus.visibility = View.VISIBLE
        } else {
            tvAddressStatus.visibility = View.GONE
        }
        tvAddressName.text = Utils.getHtmlFormat(recipientAddress?.addrName)
        tvRecipientName.text = Utils.getHtmlFormat(recipientAddress?.receiverName)
        tvRecipientPhone.text = recipientAddress?.phone
        tvRecipientAddress.text = Utils.getHtmlFormat(getFullAddress(recipientAddress))

        tvChangeRecipientAddress.setOnClickListener {
            //TODO change address activity
        }
    }

    private fun getFullAddress(recipientAddress: KeroGetAddress.Data?): String {
        return (recipientAddress?.address1 + ", "
                + recipientAddress?.districtName + ", "
                + recipientAddress?.cityName + ", "
                + recipientAddress?.provinceName)
    }

    override fun getViewModelType(): Class<MoneyInCheckoutViewModel> {
        return MoneyInCheckoutViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel?) {
        moneyInCheckoutViewModel = viewModel as MoneyInCheckoutViewModel
    }

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getTncFragmentInstance(TncResId: Int): Fragment? {
        return null
    }

    override fun getBottomSheetLayoutRes(): Int {
        return -1
    }

    override fun doNeedReattach(): Boolean {
        return false
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_money_in_checkout
    }
}