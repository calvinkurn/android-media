package com.tokopedia.feedplus.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.content.common.report_content.bottomsheet.FeedThreeDotsMenuBottomSheet
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment : BaseDaggerFragment(), FeedListener {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var feedMainViewModel: FeedMainViewModel
    private lateinit var feedMenuSheet: FeedThreeDotsMenuBottomSheet


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            feedMainViewModel = viewModelProvider.get(FeedMainViewModel::class.java)
        }

        arguments?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        } ?: savedInstanceState?.let {
            data = it.getParcelable(ARGUMENT_DATA)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(ARGUMENT_DATA, data)
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
        if (::feedMenuSheet.isInitialized) {
            feedMenuSheet.dismiss()
        }
    }

    override fun initInjector() {
        FeedMainInjector.get(requireContext()).inject(this)
    }

    override fun getScreenName(): String = "Feed Fragment"

    private fun initView() {
        binding?.let {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = FeedPostAdapter(FeedAdapterTypeFactory(this))

            LinearSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter

            adapter?.addElement(
                listOf(
                    FeedModel("Post 1"),
                    FeedModel("Post 2"),
                    FeedModel("Post 3"),
                    FeedModel("Post 4"),
                    FeedModel("Post 5"),
                )
            )

//            it.tvTest.text = data?.title ?: ""
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

    override fun onMenuClicked(model: FeedModel) {
        //TODO add implementation to open menu sheet
    }

}
