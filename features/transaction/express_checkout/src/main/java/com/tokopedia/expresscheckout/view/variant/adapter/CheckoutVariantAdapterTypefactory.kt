package com.tokopedia.expresscheckout.view.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantAdapterTypefactory : BaseAdapterTypeFactory(), CheckoutVariantTypeFactory {

    override fun type(viewModel: CheckoutVariantNoteViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: CheckoutVariantProductViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: CheckoutVariantProfileViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: CheckoutVariantQuantityViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: CheckoutVariantSummaryViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(viewModel: CheckoutVariantTypeVariantViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}