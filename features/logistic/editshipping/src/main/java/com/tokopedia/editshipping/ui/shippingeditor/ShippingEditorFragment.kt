package com.tokopedia.editshipping.ui.shippingeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorComponent
import javax.inject.Inject

class ShippingEditorFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShippingEditorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShippingEditorViewModel::class.java)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShippingEditorComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_editor_new, container, false)
    }


}