package com.tokopedia.tokopedianow.annotation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.annotation.di.component.DaggerAllAnnotationComponent
import com.tokopedia.tokopedianow.annotation.presentation.activity.TokoNowAllAnnotationActivity.Companion.KEY_ANNOTATION_ID
import com.tokopedia.tokopedianow.annotation.presentation.activity.TokoNowAllAnnotationActivity.Companion.KEY_ANNOTATION_TYPE
import com.tokopedia.tokopedianow.annotation.presentation.activity.TokoNowAllAnnotationActivity.Companion.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowAllAnnotationBinding
import com.tokopedia.tokopedianow.seeallcategory.analytic.SeeAllCategoryAnalytics
import com.tokopedia.tokopedianow.annotation.presentation.adapter.AllAnnotationAdapter
import com.tokopedia.tokopedianow.annotation.presentation.adapter.AllAnnotationAdapterTypeFactory
import com.tokopedia.tokopedianow.annotation.presentation.decoration.AllAnnotationDecoration
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel
import com.tokopedia.tokopedianow.annotation.presentation.viewholder.AnnotationViewHolder
import com.tokopedia.tokopedianow.annotation.presentation.viewmodel.TokoNowAllAnnotationViewModel
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.setupLayout
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowAllAnnotationFragment : Fragment() {
    companion object {
        private const val SPAN_COUNT = 3

        fun newInstance(
            categoryId: String?,
            annotationId: String?,
            annotationType: String?
        ): TokoNowAllAnnotationFragment = TokoNowAllAnnotationFragment().apply {
            val bundle = Bundle()
            bundle.putString(KEY_CATEGORY_ID, categoryId)
            bundle.putString(KEY_ANNOTATION_ID, annotationId)
            bundle.putString(KEY_ANNOTATION_TYPE, annotationType)
            arguments = bundle
        }
    }

    @Inject
    lateinit var viewModel: TokoNowAllAnnotationViewModel

    @Inject
    lateinit var analytics: SeeAllCategoryAnalytics

    private val categoryId: String
        by lazy { arguments?.getString(KEY_CATEGORY_ID).orEmpty() }
    private val annotationType: String
        by lazy { arguments?.getString(KEY_ANNOTATION_TYPE).orEmpty() }

    private var binding: FragmentTokopedianowAllAnnotationBinding?
        by autoClearedNullable()
    private var adapter: AllAnnotationAdapter?
        by autoClearedNullable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowAllAnnotationBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            setupUi()
            observeLiveData()
            getFirstPage()
        }
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun FragmentTokopedianowAllAnnotationBinding.setupUi() {
        setupHeader()
        setupRecyclerView()
    }

    private fun FragmentTokopedianowAllAnnotationBinding.setupHeader() {
        header.apply {
            isShowShadow = false
            setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun FragmentTokopedianowAllAnnotationBinding.setupRecyclerView() {
        adapter = AllAnnotationAdapter(AllAnnotationAdapterTypeFactory(annotationCallback()))
        rvAllAnnotation.apply {
            addItemDecoration(AllAnnotationDecoration(getDpFromDimen(context, unifyprinciplesR.dimen.unify_space_16).toIntSafely()))
            adapter = this@TokoNowAllAnnotationFragment.adapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
        }
    }

    private fun FragmentTokopedianowAllAnnotationBinding.observeLiveData() {
        viewModel.annotationList.observe(viewLifecycleOwner) { result ->
            hideShimmering()

            when (result) {
                is Success -> showLayout(result.data)
                is Fail -> showGlobalError(result.throwable)
            }
        }

        viewModel.headerTitle.observe(viewLifecycleOwner) { result ->
            header.isShowShadow = false
            header.headerTitle = if (result is Success && result.data.isNotBlank()) result.data else "Semua brand"
        }
    }

    private fun FragmentTokopedianowAllAnnotationBinding.showGlobalError(throwable: Throwable?) {
        header.isShowShadow = true
        rvAllAnnotation.hide()
        errorState.show()
        errorState.setupLayout(
            throwable = throwable,
            onClickRetryListener = {
                getFirstPage()
            }
        )
    }

    private fun FragmentTokopedianowAllAnnotationBinding.showLayout(annotationList: List<AnnotationUiModel>) {
        rvAllAnnotation.show()
        adapter?.submitList(annotationList)
    }

    private fun FragmentTokopedianowAllAnnotationBinding.hideShimmering() {
        loadingState.root.hide()
        errorState.hide()
    }

    private fun FragmentTokopedianowAllAnnotationBinding.showShimmering() {
        loadingState.root.show()
        rvAllAnnotation.hide()
        errorState.hide()
    }

    private fun initInjector() {
        DaggerAllAnnotationComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun FragmentTokopedianowAllAnnotationBinding.getFirstPage() {
        showShimmering()
        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )
    }

    private fun annotationCallback() = object : AnnotationViewHolder.AnnotationListener {
        override fun onClick(data: AnnotationUiModel, position: Int) {
            //track
        }

        override fun onImpress(data: AnnotationUiModel, position: Int) {
            //track
        }
    }
}
