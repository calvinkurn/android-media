package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.db.District
import com.tokopedia.logisticCommon.data.response.DistrictItem
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.databinding.FragmentAddressFormBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressFragment
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class AddressFormFragment : BaseDaggerFragment() {

    private var saveDataModel: SaveAddressDataModel? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0

    private var binding by autoCleared<FragmentAddressFormBinding>()

    private val viewModel: AddressFormViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddressFormViewModel::class.java)
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getScreenName(): String  = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddressFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            saveDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            currentLat = saveDataModel?.latitude?.toDouble() ?: 0.0
            currentLong = saveDataModel?.longitude?.toDouble() ?: 0.0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData()
        initObserver()
    }

    private fun prepareData() {
        viewModel.getDistrictDetail(saveDataModel?.districtId.toString())
    }

    private fun initObserver() {
        viewModel.districtDetail.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    prepareLayout(it.data.district[0])
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        })

        viewModel.saveAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == 1) {
                        saveDataModel?.id = it.data.addrId
                        onSuccessAddAddress()
                    }
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun prepareLayout(data: DistrictItem) {
        binding.run {
            cardAddress.addressDistrict.text = "${data.districtName}, ${data.cityName}, ${data.provinceName}"

            formAddress.etLabel.textFieldInput.setText("Rumah")

            formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)

        }

        binding.btnSaveAddress.setOnClickListener {
            doSaveAddress()
        }
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let { viewModel.saveAddress(it) }
    }

    private fun setSaveAddressDataModel() {
        saveDataModel?.address1 = "${binding.formAddress.etAlamat.textFieldInput.text} (${binding.formAddress.etCourierNote.textFieldInput.text})"
        saveDataModel?.address2 = "$currentLat,$currentLong"
        saveDataModel?.addressName =  binding.formAddress.etLabel.textFieldInput.text.toString()
        saveDataModel?.receiverName = binding.formAccount.etNamaPenerima.textFieldInput.text.toString()
        saveDataModel?.phone = binding.formAccount.etNomorHp.textFieldInput.text.toString()
    }

    private fun onSuccessAddAddress() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(AddEditAddressFragment.EXTRA_ADDRESS_NEW, saveDataModel)
            })
            finish()
        }
    }

    companion object {

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                }
            }
        }
    }

}