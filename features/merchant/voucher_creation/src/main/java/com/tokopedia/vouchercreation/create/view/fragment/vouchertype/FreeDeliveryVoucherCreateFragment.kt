package com.tokopedia.vouchercreation.create.view.fragment.vouchertype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldType
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.FreeDeliveryVoucherCreateViewModel
import javax.inject.Inject

class FreeDeliveryVoucherCreateFragment(onNextStep: () -> Unit): BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(onNextStep: () -> Unit = {}) = FreeDeliveryVoucherCreateFragment(onNextStep)


    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(FreeDeliveryVoucherCreateViewModel::class.java)
    }

    private val promoDescTickerModel by lazy {
        PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_free_deliv_ticker, ::onDismissTicker)
    }

    private val freeDeliveryAmountTextFieldModel by lazy {
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_free_deliv_textfield_free_deliv_amount,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.NOMINAL_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.NOMINAL_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                promotionType = PromotionType.FreeDelivery.Amount,
                onValueChanged = ::onTextFieldValueChanged)
    }

    private val minimumPurchaseTextFieldModel by lazy {
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.PUCHASE_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.PUCHASE_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                promotionType = PromotionType.FreeDelivery.MinimumPurchase,
                onValueChanged = ::onTextFieldValueChanged)
    }

    private val voucherQuotaTextFieldModel by lazy {
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.QUANTITY,
                labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.VOUCHER_QUOTA,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.VOUCHER_QUOTA,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                promotionType = PromotionType.FreeDelivery.VoucherQuota,
                onValueChanged = ::onTextFieldValueChanged)
    }

    private val promotionExpenseEstimationUiModel =
            PromotionExpenseEstimationUiModel()

    private val freeDeliveryTypeUiList = mutableListOf(
            promoDescTickerModel,
            freeDeliveryAmountTextFieldModel,
            minimumPurchaseTextFieldModel,
            voucherQuotaTextFieldModel,
            promotionExpenseEstimationUiModel,
            NextButtonUiModel(onNextStep)
    )

    override fun getAdapterTypeFactory(): PromotionTypeItemAdapterFactory = PromotionTypeItemAdapterFactory()

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_free_delivery_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setupView() {
        viewLifecycleOwner.observe(viewModel.expensesExtimationLiveData) { value ->
            promotionExpenseEstimationUiModel.estimationValue = value
            adapter.run {
                notifyItemChanged(data.indexOf(promotionExpenseEstimationUiModel))
            }
        }
        renderList(freeDeliveryTypeUiList)
    }

    private fun onDismissTicker() {
        adapter.data.remove(promoDescTickerModel)
        adapter.notifyDataSetChanged()
    }

    private fun onTextFieldValueChanged(value: Int?, typeType: PromotionType) {
        if (typeType is PromotionType.FreeDelivery) {
            viewModel.addTextFieldValueToCalculation(value, typeType)
        }
    }

}