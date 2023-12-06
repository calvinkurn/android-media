package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.product.preview.databinding.FragmentReviewBinding
import javax.inject.Inject

class ReviewFragment @Inject constructor(): TkpdBaseV4Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

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
    }

    companion object {
        const val TAG = "ReviewFragment"

        fun getOrCreate(
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
