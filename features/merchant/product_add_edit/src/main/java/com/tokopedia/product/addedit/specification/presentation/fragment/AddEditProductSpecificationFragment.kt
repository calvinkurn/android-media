package com.tokopedia.product.addedit.specification.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.specification.di.DaggerAddEditProductSpecificationComponent
import com.tokopedia.product.addedit.specification.presentation.viewmodel.AddEditProductSpecificationViewModel
import javax.inject.Inject

class AddEditProductSpecificationFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModel: AddEditProductSpecificationViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerAddEditProductSpecificationComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder
                        .getComponent(requireContext().applicationContext as BaseMainApplication))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_edit_product_shipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
