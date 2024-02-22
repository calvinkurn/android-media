package com.tokopedia.tokopedianow.annotation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics
import com.tokopedia.tokopedianow.annotation.di.component.DaggerAllAnnotationComponent
import com.tokopedia.tokopedianow.annotation.di.module.AllAnnotationModule
import com.tokopedia.tokopedianow.annotation.presentation.activity.TokoNowAllAnnotationActivity.Companion.KEY_ANNOTATION_TYPE
import com.tokopedia.tokopedianow.annotation.presentation.activity.TokoNowAllAnnotationActivity.Companion.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowAllAnnotationBinding
import com.tokopedia.tokopedianow.annotation.presentation.adapter.AllAnnotationAdapter
import com.tokopedia.tokopedianow.annotation.presentation.adapter.AllAnnotationAdapterTypeFactory
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel
import com.tokopedia.tokopedianow.annotation.presentation.viewholder.AnnotationViewHolder
import com.tokopedia.tokopedianow.annotation.presentation.viewmodel.TokoNowAllAnnotationViewModel
import com.tokopedia.tokopedianow.common.decoration.SeeAllPageDecoration
import com.tokopedia.tokopedianow.common.util.GlobalErrorUtil.setupLayout
import com.tokopedia.tokopedianow.common.viewholder.TokoNowLoadingMoreViewHolder
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowAllAnnotationFragment : Fragment() {
    companion object {
        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1

        fun newInstance(
            categoryId: String?,
            annotationType: String?
        ): TokoNowAllAnnotationFragment = TokoNowAllAnnotationFragment().apply {
            val bundle = Bundle()
            bundle.putString(KEY_CATEGORY_ID, categoryId)
            bundle.putString(KEY_ANNOTATION_TYPE, annotationType)
            arguments = bundle
        }
    }

    @Inject
    lateinit var viewModel: TokoNowAllAnnotationViewModel

    @Inject
    lateinit var analytics: AllAnnotationAnalytics

    internal val categoryId: String
        by lazy { arguments?.getString(KEY_CATEGORY_ID).orEmpty() }
    internal val annotationType: String
        by lazy { arguments?.getString(KEY_ANNOTATION_TYPE).orEmpty() }

    private val loadMoreListener: RecyclerView.OnScrollListener
        by lazy { createLoadMoreListener() }

    private var binding: FragmentTokopedianowAllAnnotationBinding?
        by autoClearedNullable()
    private var adapter: AllAnnotationAdapter?
        by autoClearedNullable()
    private var layoutManager: GridLayoutManager?
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
                analytics.trackClickBackAllAnnotationPage()
            }
        }
    }

    private fun FragmentTokopedianowAllAnnotationBinding.setupRecyclerView() {
        adapter = AllAnnotationAdapter(AllAnnotationAdapterTypeFactory(annotationCallback()))
        layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter?.getItemViewType(position)) {
                        AnnotationViewHolder.LAYOUT -> SPAN_FULL_SPACE
                        else -> SPAN_COUNT
                    }
                }
            }
        }
        rvAllAnnotation.apply {
            addItemDecoration(SeeAllPageDecoration(getDpFromDimen(context, unifyprinciplesR.dimen.unify_space_16).toIntSafely()))
            addOnScrollListener(loadMoreListener)
            animation = null
            adapter = this@TokoNowAllAnnotationFragment.adapter
            layoutManager = this@TokoNowAllAnnotationFragment.layoutManager
        }
    }

    private fun FragmentTokopedianowAllAnnotationBinding.observeLiveData() {
        observe(viewModel.firstPage) { result ->
            hideShimmering()

            when (result) {
                is Success -> {
                    loadLayout(result.data)
                    analytics.trackImpressAllAnnotationPage()
                }
                is Fail -> showGlobalError(result.throwable)
            }
        }

        observe(viewModel.loadMore) { result ->
            loadLayout(result)
        }

        observe(viewModel.isOnScrollNotNeeded) {
            rvAllAnnotation.removeOnScrollListener(loadMoreListener)
        }

        observe(viewModel.headerTitle) { result ->
            header.isShowShadow = false
            header.headerTitle = if (result is Success && result.data.isNotBlank()) result.data else getString(R.string.tokopedianow_all_annotation_page_title)
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

    private fun FragmentTokopedianowAllAnnotationBinding.hideShimmering() {
        loadingState.root.hide()
        errorState.hide()
        rvAllAnnotation.show()
    }

    private fun FragmentTokopedianowAllAnnotationBinding.showShimmering() {
        loadingState.root.show()
        rvAllAnnotation.hide()
        errorState.hide()
    }

    private fun initInjector() {
        DaggerAllAnnotationComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .allAnnotationModule(AllAnnotationModule(categoryId, annotationType))
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

    private fun loadLayout(annotationList: List<Visitable<*>>) {
        adapter?.submitList(annotationList)
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisiblePosition = layoutManager?.findLastVisibleItemPosition()
            if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                val lastVisibleViewHolder = recyclerView.findViewHolderForAdapterPosition(lastVisiblePosition.orZero())
                viewModel.loadMore(
                    categoryId,
                    annotationType,
                    lastVisibleViewHolder is TokoNowLoadingMoreViewHolder
                )
            }
        }
    }

    private fun annotationCallback(): AnnotationViewHolder.AnnotationListener = object : AnnotationViewHolder.AnnotationListener {
        override fun onClick(data: AnnotationUiModel, position: Int) {
            analytics.trackClickAnnotationCard(
                annotationValue = data.name
            )
        }

        override fun onImpress(data: AnnotationUiModel, position: Int) {
            analytics.trackImpressAnnotationCard(
                annotationValue = data.name
            )
        }
    }
}
