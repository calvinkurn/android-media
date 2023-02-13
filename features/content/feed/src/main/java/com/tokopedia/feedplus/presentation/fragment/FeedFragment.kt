package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.model.FeedDataModel

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment : BaseDaggerFragment() {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            data = it.getParcelable(ARGUMENT_DATA, FeedDataModel::class.java)
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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun initInjector() {
//        TODO("Not yet implemented")
    }

    override fun getScreenName(): String = "Feed Fragment"

    private fun initView() {
        binding?.let {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = FeedPostAdapter(FeedAdapterTypeFactory())

            LinearSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter

//            adapter?.addElement(
//                listOf(
//                    DummyModel("Post 1 - ${feedType.value}"),
//                    DummyModel("Post 2 - ${feedType.value}"),
//                    DummyModel("Post 3 - ${feedType.value}"),
//                    DummyModel("Post 4 - ${feedType.value}"),
//                    DummyModel("Post 5 - ${feedType.value}"),
//                )
//            )
        }
    }

    companion object {
        private const val ARGUMENT_DATA = "ARGUMENT_DATA"

        fun createFeedFragment(data: FeedDataModel): FeedFragment =
            FeedFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(ARGUMENT_DATA, data)
                }
            }
    }
}
