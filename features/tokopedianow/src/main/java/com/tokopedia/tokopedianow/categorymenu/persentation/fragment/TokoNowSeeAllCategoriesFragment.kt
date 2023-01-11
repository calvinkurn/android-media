package com.tokopedia.tokopedianow.categorymenu.persentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.categorymenu.persentation.viewmodel.TokoNowSeeAllCategoriesViewModel
import com.tokopedia.tokopedianow.categorylist.presentation.activity.TokoNowCategoryListActivity
import com.tokopedia.tokopedianow.categorymenu.di.component.DaggerSeeAllCategoriesComponent
import com.tokopedia.tokopedianow.categorymenu.persentation.adapter.SeeAllCategoriesAdapter
import com.tokopedia.tokopedianow.categorymenu.persentation.adapter.SeeAllCategoriesAdapterTypeFactory
import com.tokopedia.tokopedianow.categorymenu.persentation.decoration.SeeAllCategoriesDecoration
import com.tokopedia.tokopedianow.categorymenu.persentation.uimodel.SeeAllCategoriesItemUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowCategoryMenuBinding
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoNowSeeAllCategoriesFragment: Fragment() {

    companion object {
        private const val ERROR_STATE_NOT_FOUND_IMAGE_URL = "https://images.tokopedia.net/img/error_page_400_category_list.png"
        private const val SPAN_COUNT = 3

        fun newInstance(warehouseId: String): TokoNowSeeAllCategoriesFragment {
            return TokoNowSeeAllCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(TokoNowCategoryListActivity.PARAM_WAREHOUSE_ID, warehouseId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelTokoNow: TokoNowSeeAllCategoriesViewModel

    private var binding by autoClearedNullable<FragmentTokopedianowCategoryMenuBinding>()
    private var adapter by autoClearedNullable<SeeAllCategoriesAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowCategoryMenuBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            setupHeader()
            setupRecyclerView()
        }

        observeLiveData()
        getCategoryList()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupHeader() {
        huCategoryMenu.apply {
            isShowShadow = false
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun FragmentTokopedianowCategoryMenuBinding.setupRecyclerView() {
        adapter = SeeAllCategoriesAdapter(SeeAllCategoriesAdapterTypeFactory())
        rvCategoryMenu.apply {
            addItemDecoration(SeeAllCategoriesDecoration(getDpFromDimen(context, com.tokopedia.unifyprinciples.R.dimen.unify_space_16).toIntSafely()))
            adapter = this@TokoNowSeeAllCategoriesFragment.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }

    private fun initInjector() {
        DaggerSeeAllCategoriesComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeLiveData() {
        observe(viewModelTokoNow.categoryList) {
            when(it) {
                is Success -> {
                    if(it.data.header.errorCode.isNullOrEmpty()){
                        val categoryMenus = it.data.data.map {
                            SeeAllCategoriesItemUiModel(
                                id = it.id,
                                title = it.name,
                                appLink = it.appLinks,
                                imageUrl = it.imageUrl
                            )
                        }
                        adapter?.submitList(categoryMenus)
                    }
                }
            }
        }
    }

    private fun getCategoryList() {
        val warehouseId = arguments?.getString(TokoNowCategoryListActivity.PARAM_WAREHOUSE_ID).orEmpty()
        viewModelTokoNow.getCategoryList(warehouseId)
    }

}
