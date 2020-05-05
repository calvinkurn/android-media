package com.tokopedia.vouchercreation.create.view.fragment.step

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
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

    private val generalExpensesInfoBottomSheetFragment by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context).apply {
            setTitle(context?.resources?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
        }
    }

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {

    }

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {

    }

    override fun getAdapterTypeFactory(): PromotionTypeBudgetAdapterTypeFactory = PromotionTypeBudgetAdapterTypeFactory(this)

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}

    override fun setupView() {
        super.setupView()
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        context?.run {
            addBottomSheetView(CreateVoucherBottomSheetType.GENERAL_EXPENSE_INFO, generalExpensesInfoBottomSheetFragment)
        }
    }

    override var extraWidget: List<Visitable<PromotionTypeBudgetTypeFactory>> =
            listOf(promotionTypeInputWidget)
}