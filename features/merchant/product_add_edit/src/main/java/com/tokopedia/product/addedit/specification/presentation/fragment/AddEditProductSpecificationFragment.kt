package com.tokopedia.product.addedit.specification.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.di.DaggerAddEditProductSpecificationComponent
import com.tokopedia.product.addedit.specification.presentation.viewmodel.AddEditProductSpecificationViewModel
import javax.inject.Inject

class AddEditProductSpecificationFragment: BaseDaggerFragment() {

    companion object {
        fun createInstance(cacheManagerId: String): Fragment {
            return AddEditProductSpecificationFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_CACHE_MANAGER_ID, cacheManagerId) }
            }
        }
    }

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
        val cacheManagerId = arguments?.getString(EXTRA_CACHE_MANAGER_ID).orEmpty()
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
        val productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java, ProductInputModel())

        viewModel.setProductInputModel(productInputModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_edit_product_shipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeProductInputModel()
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(viewLifecycleOwner, Observer {
            viewModel.getSpecifications(it.detailInputModel.categoryId)
        })
    }
}
