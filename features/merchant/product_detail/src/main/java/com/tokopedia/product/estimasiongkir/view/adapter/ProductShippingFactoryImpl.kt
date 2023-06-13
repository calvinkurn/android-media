package com.tokopedia.product.estimasiongkir.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.product.estimasiongkir.data.model.shipping.FreeShippingDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingErrorDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingSellyDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingShimmerDataModel
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.FreeShippingViewHolder
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingErrorViewHolder
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingHeaderViewHolder
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingSellyViewHolder
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingServiceViewHolder
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingShimmerViewHolder
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingListener

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductShippingFactoryImpl(val listener: ProductDetailShippingListener,
                                 private val chooseAddressListener: ChooseAddressWidget.ChooseAddressWidgetListener) : BaseAdapterTypeFactory(), ProductShippingFactory {
    override fun type(dataProduct: ProductShippingHeaderDataModel): Int {
        return ProductShippingHeaderViewHolder.LAYOUT
    }

    override fun type(dataProduct: ProductShippingServiceDataModel): Int {
        return ProductShippingServiceViewHolder.LAYOUT
    }

    override fun type(dataProduct: ProductShippingShimmerDataModel): Int {
        return ProductShippingShimmerViewHolder.LAYOUT
    }

    override fun type(dataProduct: ProductShippingErrorDataModel): Int {
        return ProductShippingErrorViewHolder.LAYOUT
    }

    override fun type(dataProduct: ProductShippingSellyDataModel): Int {
        return ProductShippingSellyViewHolder.LAYOUT
    }

    override fun type(dataProduct: FreeShippingDataModel): Int {
        return FreeShippingViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductShippingHeaderViewHolder.LAYOUT -> ProductShippingHeaderViewHolder(view, listener, chooseAddressListener)
            ProductShippingServiceViewHolder.LAYOUT -> ProductShippingServiceViewHolder(view)
            ProductShippingShimmerViewHolder.LAYOUT -> ProductShippingShimmerViewHolder(view)
            ProductShippingErrorViewHolder.LAYOUT -> ProductShippingErrorViewHolder(view, listener)
            ProductShippingSellyViewHolder.LAYOUT -> ProductShippingSellyViewHolder(view, listener)
            FreeShippingViewHolder.LAYOUT -> FreeShippingViewHolder(view)
            else -> return super.createViewHolder(view, type)
        }
    }
}
