package com.tokopedia.feedplus.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.report_content.bottomsheet.FeedThreeDotsMenuBottomSheet
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.feedplus.databinding.FragmentFeedImmersiveBinding
import com.tokopedia.feedplus.di.FeedMainInjector
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.adapter.FeedPostAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMenuIdentifier
import com.tokopedia.feedplus.presentation.model.FeedMenuItem
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.feedplus.presentation.viewmodel.FeedMainViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedR

/**
 * Created By : Muhammad Furqan on 01/02/23
 */
class FeedFragment : BaseDaggerFragment(), FeedListener, FeedThreeDotsMenuBottomSheet.Listener {

    private var binding: FragmentFeedImmersiveBinding? = null

    private var data: FeedDataModel? = null
    private var adapter: FeedPostAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    internal lateinit var userSession: UserSessionInterface

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        feedMainViewModel.run {
            reportResponse.observe(lifecycleOwner) {
                when (it) {
                    is Success -> {
                        if (::feedMenuSheet.isInitialized)
                            feedMenuSheet.setFinalView()
                    }
                    is Fail -> Toast.makeText(context, "Laporkan fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {
            REQUEST_REPORT_POST_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                if (::feedMenuSheet.isInitialized) {
                    feedMenuSheet.showReportLayoutWhenLaporkanClicked()
                    feedMenuSheet.showToasterOnLoginSuccessFollow(
                        getString(feedR.string.feed_report_login_success_toaster_text),
                        Toaster.TYPE_NORMAL
                    )
                }
            }
            else -> {
            }
        }
    }


    private fun initView() {
        binding?.let {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = FeedPostAdapter(FeedAdapterTypeFactory(this))

            LinearSnapHelper().attachToRecyclerView(it.rvFeedPost)
            it.rvFeedPost.layoutManager = layoutManager
            it.rvFeedPost.adapter = adapter

            adapter?.addElement(
                listOf(
                    FeedModel(text = "Post 1"),
                    FeedModel(text = "Post 2"),
                    FeedModel(text = "Post 3"),
                    FeedModel(text = "Post 4"),
                    FeedModel(text = "Post 5"),
                )
            )

//            it.tvTest.text = data?.title ?: ""
        }
    }

    companion object {
        private const val ARGUMENT_DATA = "ARGUMENT_DATA"
        const val REQUEST_REPORT_POST_LOGIN = 1201


        fun createFeedFragment(data: FeedDataModel): FeedFragment =
            FeedFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(ARGUMENT_DATA, data)
                }
            }
    }

    private fun onGoToLogin() {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            requireActivity().startActivityForResult(intent, REQUEST_REPORT_POST_LOGIN)
        }
    }

    override fun onMenuClicked(model: FeedModel) {
        activity?.let {
            feedMenuSheet = FeedThreeDotsMenuBottomSheet
                .getFragment(it.supportFragmentManager, it.classLoader)
            feedMenuSheet.setListener(this)
            feedMenuSheet.setData(getMenuItemData(), model.id)
            feedMenuSheet.show(it.supportFragmentManager)
        }
    }

    private fun getMenuItemData() : List<FeedMenuItem> {
        val items = arrayListOf<FeedMenuItem>()

        items.add(
            FeedMenuItem(
                drawable = getIconUnifyDrawable(requireContext(), IconUnify.VISIBILITY),
                name = getString(feedR.string.feed_clear_mode),
                type = FeedMenuIdentifier.MODE_NONTON
            )
        )
        items.add(
            FeedMenuItem(
                drawable = getIconUnifyDrawable(
                    requireContext(), IconUnify.WARNING, MethodChecker.getColor(
                        context,
                        R.color.Unify_RN500
                    )
                ),
                name = getString(feedR.string.feed_report),
                type = FeedMenuIdentifier.LAPORKAN
            )
        )
        return items

    }

    override fun onMenuItemClick(feedMenuItem: FeedMenuItem) {
        when(feedMenuItem.type){
            FeedMenuIdentifier.LAPORKAN -> {
                if (!userSession.isLoggedIn) {
                    onGoToLogin()
                } else {
                    feedMenuSheet.showReportLayoutWhenLaporkanClicked()
                    Toast.makeText(context, "Laporkan - onMenuItemClick", Toast.LENGTH_SHORT).show()
                }
            }

            FeedMenuIdentifier.MODE_NONTON -> {
                //TODO add code for clear mode
                Toast.makeText(context, "Mode Nonton", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onReportPost(feedReportRequestParamModel: FeedReportRequestParamModel) {
        feedMainViewModel.reportContent(feedReportRequestParamModel)
    }
}
