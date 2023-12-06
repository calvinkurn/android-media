package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.databinding.FragmentReviewBinding
import com.tokopedia.content.product.preview.view.adapter.review.ReviewParentAdapter
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.viewmodel.EntrySource
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ReviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        viewModelFactory.create(
            EntrySource(productId = "4937529690") //Testing purpose
        )
    }

    private val reviewAdapter by lazy {
        ReviewParentAdapter()
    }

    override fun getScreenName() = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeReview()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getReview()
    }

    private fun setupView() {
        binding.rvReview.adapter = reviewAdapter
    }

    private fun observeReview() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.review.withCache().collectLatest { (prev, curr) ->
                renderList(prev, curr)
            }
        }
    }

    private fun renderList(prev: List<ReviewUiModel>?, data: List<ReviewUiModel>) {
        if (prev == null || prev == data) return
        reviewAdapter.submitList(data)
    }

    companion object {
        const val TAG = "ReviewFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ReviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ReviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ReviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ReviewFragment
        }
    }
}
