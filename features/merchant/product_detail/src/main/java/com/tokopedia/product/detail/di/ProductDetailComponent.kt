package com.tokopedia.product.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.view.activity.ProductFullDescriptionTabActivity
import com.tokopedia.product.detail.view.activity.ProductInstallmentActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.product.report.di.ProductReportModule
import com.tokopedia.product.report.view.dialog.ReportDialogFragment
import com.tokopedia.product.warehouse.di.ProductWarehouseModule
import dagger.Component

@ProductDetailScope
@Component(modules = [
    ProductDetailModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class,
    ProductReportModule::class,
    ProductWarehouseModule::class,
    ProductWishlistModule::class],
        dependencies = [BaseAppComponent::class])
interface ProductDetailComponent {
    fun inject(fragment: ProductDetailFragment)
    fun inject(fragment: ReportDialogFragment)
    fun inject(fragment: DynamicProductDetailFragment)
    fun inject(activity: ProductInstallmentActivity)
    fun inject(bottomSheetFragment: AddToCartDoneBottomSheet)
    fun inject(activity: ProductFullDescriptionTabActivity)
}