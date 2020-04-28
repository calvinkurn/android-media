package com.tokopedia.vouchercreation.create.view.fragment.step

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTargetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTargetTypeFactory

class PromotionBudgetAndTypeFragment(onNextStep: () -> Unit = {})
    : BaseCreateMerchantVoucherFragment<VoucherTargetTypeFactory, VoucherTargetAdapterTypeFactory>(onNextStep) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit) = PromotionBudgetAndTypeFragment(onNext)
    }

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdapterTypeFactory(): VoucherTargetAdapterTypeFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}