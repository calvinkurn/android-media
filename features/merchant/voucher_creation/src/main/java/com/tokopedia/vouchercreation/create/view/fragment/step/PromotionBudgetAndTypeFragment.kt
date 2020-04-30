package com.tokopedia.vouchercreation.create.view.fragment.step

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel

class PromotionBudgetAndTypeFragment(onNextStep: () -> Unit = {})
    : BaseCreateMerchantVoucherFragment<PromotionTypeBudgetTypeFactory, PromotionTypeBudgetAdapterTypeFactory>(onNextStep, false) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit) = PromotionBudgetAndTypeFragment(onNext)
    }

    private val promotionTypeInputWidget by lazy {
        PromotionTypeInputUiModel()
    }

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdapterTypeFactory(): PromotionTypeBudgetAdapterTypeFactory = PromotionTypeBudgetAdapterTypeFactory(this)

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}

    override fun setupView() {
        super.setupView()
    }

    override var extraWidget: List<Visitable<PromotionTypeBudgetTypeFactory>> =
            listOf(promotionTypeInputWidget)
}