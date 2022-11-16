package com.tokopedia.people.views.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.people.ErrorMessage
import com.tokopedia.people.Loading
import com.tokopedia.people.R
import com.tokopedia.people.Success
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentVideoBinding
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.UserProfileActivity
import com.tokopedia.people.views.activity.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.people.views.adapter.UserPostBaseAdapter
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.LOADING
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_CONTENT
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_EMPTY
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_ERROR
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.PAGE_LOADING
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.REQUEST_CODE_LOGIN_TO_SET_REMINDER
import com.tokopedia.people.views.fragment.UserProfileFragment.Companion.REQUEST_CODE_PLAY_ROOM
import com.tokopedia.people.views.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyR

class UserProfileVideoFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userSession: UserSessionInterface,
    private var userProfileTracker: UserProfileTracker,
) : TkpdBaseV4Fragment(), AdapterCallback, UserPostBaseAdapter.PlayWidgetCallback {

    private val gridLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        GridLayoutManager(activity, 2)
    }

    private var _binding: UpFragmentVideoBinding? = null
    private val binding: UpFragmentVideoBinding
        get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            this,
            requireArguments().getString(EXTRA_USERNAME) ?: "",
        )
    }

    private val mAdapter: UserPostBaseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserPostBaseAdapter(this, this) { cursor ->
            submitAction(UserProfileAction.LoadPlayVideo(cursor))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = UpFragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initListener()
        setupPlayVideo()

        fetchPlayVideo(viewModel.profileUserID)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchPlayVideo(userId: String) {
        mAdapter.resetAdapter()
        mAdapter.cursor = ""
        mAdapter.startDataLoading(userId)
    }

    private fun getSpanSizeLookUp(): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter.getItemViewType(position)) {
                    LOADING -> 2
                    else -> 1
                }
            }
        }
    }

    private fun initObserver() {
        observeUiEvent()

        addListObserver()
        adduserPostErrorObserver()
    }

    private fun initListener() {
        (activity as? UserProfileActivity)?.initListenerPlayWidget(this)
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UserProfileUiEvent.EmptyLoadFirstVideoPosts -> {
                        if (viewModel.isSelfProfile) emptyPostSelf() else emptyPostVisitor()
                        binding.userVideoContainer.displayedChild = PAGE_EMPTY
                    }
                    is UserProfileUiEvent.SuccessUpdateReminder -> {
                        mAdapter.notifyItemChanged(event.position)
                        view?.showToast(event.message)
                    }
                    is UserProfileUiEvent.ErrorUpdateReminder -> {
                        val message = when (event.throwable) {
                            is UnknownHostException, is SocketTimeoutException -> {
                                requireContext().getString(R.string.up_error_local_error)
                            }
                            else -> {
                                event.throwable.message ?: getDefaultErrorMessage()
                            }
                        }

                        view?.showErrorToast(message)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addListObserver() {
        viewModel.playPostContentLiveData.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Loading -> {
                        mAdapter.resetAdapter()
                        mAdapter.notifyDataSetChanged()
                    }
                    is Success -> {
                        mAdapter.onSuccess(it.data)
                    }
                    is ErrorMessage -> {
                        mAdapter.onError()
                    }
                }
            }
        }
    }

    private fun adduserPostErrorObserver() {
        viewModel.userPostErrorLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                userVideoContainer.displayedChild = PAGE_ERROR

                globalErrorVideo.refreshBtn?.setOnClickListener {
                    userVideoContainer.displayedChild = PAGE_LOADING
                    fetchPlayVideo(viewModel.profileUserID)
                }
            }
        }
    }

    /** Render UI */
    private fun setupPlayVideo() {
        gridLayoutManager.spanSizeLookup = getSpanSizeLookUp()

        binding.rvPost.layoutManager = gridLayoutManager
        if (binding.rvPost.itemDecorationCount == 0) {
            val spacing =
                requireContext().resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl1)
            binding.rvPost.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))
        }
        binding.rvPost.adapter = mAdapter
    }

    private fun getDefaultErrorMessage() = getString(R.string.up_error_unknown)

    override fun getScreenName(): String = TAG

    private fun submitAction(action: UserProfileAction) {
        viewModel.submitAction(action)
    }

    private fun emptyPostSelf() {
        binding.emptyVideo.textErrorEmptyTitle.text = getString(R.string.up_empty_post_title_on_self)
        binding.emptyVideo.textErrorEmptyDesc.apply {
            text = getString(R.string.up_empty_post_desc_on_self)
            show()
        }
    }

    private fun emptyPostVisitor() {
        binding.emptyVideo.textErrorEmptyTitle.text = getString(R.string.up_empty_post_on_visitor)
        binding.emptyVideo.textErrorEmptyDesc.hide()
    }

    override fun onRetryPageLoad(pageNumber: Int) {
    }

    override fun onEmptyList(rawObject: Any?) {
        if (viewModel.isSelfProfile) {
            emptyPostSelf()
        } else {
            emptyPostVisitor()
        }
        binding.userVideoContainer.displayedChild = PAGE_EMPTY
    }

    override fun onStartFirstPageLoad() {
        binding.userVideoContainer.displayedChild = PAGE_LOADING
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        binding.userVideoContainer.displayedChild = PAGE_CONTENT
    }

    override fun onStartPageLoad(pageNumber: Int) {
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
    }

    override fun onError(pageNumber: Int) {
    }

    override fun updatePostReminderStatus(channelId: String, isActive: Boolean, pos: Int) {
        submitAction(UserProfileAction.SaveReminderActivityResult(channelId, pos, isActive))

        if (userSession.isLoggedIn.not()) {
            startActivityForResult(
                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN_TO_SET_REMINDER,
            )
        } else {
            submitAction(UserProfileAction.ClickUpdateReminder(false))
        }
    }

    override fun updatePlayWidgetLatestData(
        channelId: String,
        totalView: String,
        isReminderSet: Boolean,
    ) {
        mAdapter.updatePlayWidgetLatestData(channelId, totalView, isReminderSet)
    }

    override fun onPlayWidgetLargeClick(appLink: String, channelID: String, isLive: Boolean, imageUrl: String, pos: Int) {
        userProfileTracker.clickVideo(
            viewModel.profileUserID,
            viewModel.isSelfProfile,
            isLive,
            channelID,
            imageUrl,
            pos,
        )
        val intent = RouteManager.getIntent(context, appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
    }

    companion object {
        private const val TAG = "UserProfileVideoFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): UserProfileVideoFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserProfileVideoFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserProfileVideoFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as UserProfileVideoFragment
        }
    }
}
