package com.tokopedia.vouchercreation.create.view.fragment.step

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.PromotionBudgetAndTypeViewModel
import javax.inject.Inject

class PromotionBudgetAndTypeFragment(onNextStep: () -> Unit = {})
    : BaseCreateMerchantVoucherFragment<PromotionTypeBudgetTypeFactory, PromotionTypeBudgetAdapterTypeFactory>(onNextStep, false) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit) = PromotionBudgetAndTypeFragment(onNext)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(PromotionBudgetAndTypeViewModel::class.java)
    }

    private val promotionTypeInputWidget by lazy {
        PromotionTypeInputUiModel()
    }

    private val generalExpensesInfoBottomSheetFragment by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context).apply {
            setTitle(context?.resources?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
        }
    }

    private val bannerVoucherUiModel =
            BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>(
                    null,
                    "cobaindoangini",
                    "Tumbler Starbucks 123",
                    "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2020/5/6/1479278/1479278_3bab5e93-003a-4819-a68a-421f69224a59.jpg"
            )

    override var extraWidget: List<Visitable<PromotionTypeBudgetTypeFactory>> =
            listOf(
                    bannerVoucherUiModel,
                    promotionTypeInputWidget)

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {

    }

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {

    }

    override fun getAdapterTypeFactory(): PromotionTypeBudgetAdapterTypeFactory = PromotionTypeBudgetAdapterTypeFactory(this)

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
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
}