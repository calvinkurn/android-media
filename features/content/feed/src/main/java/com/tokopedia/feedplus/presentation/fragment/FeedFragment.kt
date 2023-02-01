package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.presentation.model.FeedTabType

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment : BaseDaggerFragment() {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var feedType: FeedTabType = FeedTabType.FOR_YOU

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            feedType =
                if (it.getString(ARGUMENT_TYPE) == FeedTabType.FOR_YOU.value) FeedTabType.FOR_YOU
                else FeedTabType.FOLLOWING
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedImmersiveBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvFeed?.text = feedType.value
    }

    override fun initInjector() {
//        TODO("Not yet implemented")
    }

    override fun getScreenName(): String = "Feed Fragment"

    companion object {
        private const val ARGUMENT_TYPE = "ARGUMENT_TYPE"

        fun createFeedFragment(type: FeedTabType): FeedFragment =
            FeedFragment().also {
                it.arguments = Bundle().apply {
                    putString(ARGUMENT_TYPE, type.value)
                }
            }
    }

}
