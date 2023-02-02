package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.model.DummyModel
import com.tokopedia.feedplus.presentation.model.FeedTabType

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment : BaseDaggerFragment() {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var feedType: FeedTabType = FeedTabType.FOR_YOU
    private var adapter: FeedPostAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

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

        initView()
    }

    override fun initInjector() {
//        TODO("Not yet implemented")
    }

    override fun getScreenName(): String = "Feed Fragment"

    private fun initView() {
        binding?.let {
            layoutManager = LinearLayoutManager(context)
            adapter = FeedPostAdapter(FeedAdapterTypeFactory())

            LinearSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter

            adapter?.addElement(
                listOf(
                    DummyModel("Post 1 - ${feedType.value}"),
                    DummyModel("Post 2 - ${feedType.value}"),
                    DummyModel("Post 3 - ${feedType.value}"),
                    DummyModel("Post 4 - ${feedType.value}"),
                    DummyModel("Post 5 - ${feedType.value}"),
                )
            )
        }
    }

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
