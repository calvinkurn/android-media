package com.tokopedia.createpost.uprofile.views

import PostItemDecoration
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.*
import com.tokopedia.createpost.uprofile.di.DaggerUserProfileComponent
import com.tokopedia.createpost.uprofile.di.UserProfileModule
import com.tokopedia.createpost.uprofile.model.Profile
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.UserProfileIsFollow
import com.tokopedia.createpost.uprofile.viewmodels.UserProfileViewModel
import com.tokopedia.createpost.uprofile.views.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.feedcomponent.util.util.convertDpToPixel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.up_layout_user_profile_header.*
import javax.inject.Inject


class UserProfileFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var landedUserName: String? = null
    var isFollowed: Boolean = false
    var displayName: String = ""
    var userName: String = ""
    var totalFollowings: String = ""
    var totalFollowers: String = ""
    var userSession: UserSessionInterface? = null
    var btnAction: UnifyButton? = null

    private val mPresenter: UserProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel::class.java)
    }

    private val mAdapter: UserPostBaseAdapter by lazy {
        UserPostBaseAdapter(
            mPresenter,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        return inflater.inflate(R.layout.up_fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initListener()
        setHeader()

        if (arguments == null || requireArguments().getString(EXTRA_USERNAME).isNullOrBlank()) {
            //TODO show error page
            activity?.finish()
        }

        landedUserName = requireArguments().getString(EXTRA_USERNAME)

        landedUserName?.let {
            mPresenter.getUserDetails(it)
            userSession = UserSession(context)
        }
    }

    private fun initListener() {
        view?.findViewById<View>(R.id.text_following_label)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_following_count)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_follower_label)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_follower_count)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.img_profile_image)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_live)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.view_profile_outer_ring)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.btn_action_follow)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_see_more)?.setOnClickListener(this)
    }

    private fun initUserPost(userId: String) {
        val postRv = view?.findViewById<RecyclerView>(R.id.recycler_view)
        postRv?.layoutManager = GridLayoutManager(activity, 2)
        postRv?.addItemDecoration(PostItemDecoration(convertDpToPixel(8F, requireContext())))
        postRv?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading(userId)
    }

    private fun initObserver() {
        addUserProfileObserver()
        addListObserver()
        addDoFollowedObserver()
        addDoUnFollowedObserver()
        addTheyFollowedObserver()
    }

    private fun addUserProfileObserver() =
        mPresenter.userDetailsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> showLoader()
                    is ErrorMessage -> {
                        //TODO show error page
                    }
                    is Success -> {
                        setMainUi(it.data)
                    }
                }
            }
        })

    private fun addListObserver() =
        mPresenter.playPostContentLiveData.observe(viewLifecycleOwner, Observer {
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
        })

    private fun addDoFollowedObserver() =
        mPresenter.profileDoFollowLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        if (it.data.profileFollowers.errorCode.isBlank()) {
                            updateToFollowUi()
                            isFollowed = !isFollowed
                        } else {
                            updateToUnFollowUi()
                        }
                    }
                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addDoUnFollowedObserver() =
        mPresenter.profileDoUnFollowLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        if (it.data.profileFollowers.errorCode.isBlank()) {
                            updateToUnFollowUi()
                            isFollowed = !isFollowed
                        } else {
                            updateToFollowUi()
                        }
                    }
                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addTheyFollowedObserver() =
        mPresenter.profileTheyFollowLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Loading -> {

                    }
                    is Success -> {
                        setActionButton(it.data)
                    }
                    is ErrorMessage -> {

                    }
                }
            }
        })

    private fun addLiveClickListener(appLink: String) = View.OnClickListener {
        RouteManager.route(context, appLink)
    }

    private fun addProfileClickListener(appLink: String, userId: String) = View.OnClickListener {
        //TODO navigate to edit profile page
    }

    private fun addDoFollowClickListener(userIdEnc: String) = View.OnClickListener {
        if (isFollowed) {
            mPresenter.doUnFollow(userIdEnc)
            updateToUnFollowUi()
        } else {
            mPresenter.doFollow(userIdEnc)
            updateToFollowUi()
        }
    }

    private fun updateToFollowUi() {
        btnAction?.text = "Following"
        btnAction?.buttonVariant = UnifyButton.Variant.GHOST
        btnAction?.buttonType = UnifyButton.Type.ALTERNATE
    }

    private fun updateToUnFollowUi() {
        btnAction?.text = "Follow"
        btnAction?.buttonVariant = UnifyButton.Variant.FILLED
        btnAction?.buttonType = UnifyButton.Type.MAIN
    }

    private fun setMainUi(data: ProfileHeaderBase) {
        initUserPost(data.profileHeader.profile.userID)

        val textBio = view?.findViewById<TextView>(R.id.text_bio)
        val textUserName = view?.findViewById<TextView>(R.id.text_user_name)
        val textDisplayName = view?.findViewById<TextView>(R.id.text_display_name)
        val textContentCount = view?.findViewById<TextView>(R.id.text_content_count)
        val textFollowerCount = view?.findViewById<TextView>(R.id.text_follower_count)
        val textFollowingCount = view?.findViewById<TextView>(R.id.text_following_count)
        btnAction = view?.findViewById<UnifyButton>(R.id.btn_action_follow)

//        textBio?.text = data.profileHeader.profile.biography
        textUserName?.text = "@" + data.profileHeader.profile.username
        textDisplayName?.text = data.profileHeader.profile.name
        textContentCount?.text = data.profileHeader.stats.totalPostFmt
        textFollowerCount?.text = data.profileHeader.stats.totalFollowerFmt
        textFollowingCount?.text = data.profileHeader.stats.totalFollowingFmt

        setProfileImg(data.profileHeader.profile)

        displayName = data.profileHeader.profile.name
        userName = data.profileHeader.profile.username
        totalFollowers = data.profileHeader.stats.totalFollowerFmt
        totalFollowings = data.profileHeader.stats.totalFollowingFmt
    }

    private fun setActionButton(followProfile: UserProfileIsFollow) {

        if (followProfile.profileHeader.items[0].userID == userSession?.userId) {
            btnAction?.text = "Ubah Profil"
            btnAction?.buttonVariant = UnifyButton.Variant.GHOST
            btnAction?.buttonType = UnifyButton.Type.ALTERNATE

            btnAction?.setOnClickListener(
                addProfileClickListener(
                    "applink",
                    followProfile.profileHeader.items[0].userID
                )
            )
        } else {
            isFollowed = followProfile.profileHeader.items[0].status
            if (isFollowed) {
                updateToFollowUi()
            } else {
                updateToUnFollowUi()
            }

            btnAction?.setOnClickListener(addDoFollowClickListener(followProfile.profileHeader.items[0].encryptedUserID))
        }
    }

    private fun setProfileImg(profile: Profile) {
        if (profile == null
            || profile.liveplaychannel == null
            || profile.liveplaychannel.liveplaychannellink == null
        ) {
            return
        }

        val textLive = view?.findViewById<TextView>(R.id.text_live)
        val viewLiveRing = view?.findViewById<View>(R.id.view_profile_outer_ring)
        val imgProfile = view?.findViewById<ImageUnify>(R.id.img_profile_image)

        imgProfile?.urlSrc = profile.imageCover

        if (profile.liveplaychannel.islive) {
            viewLiveRing?.show()
            textLive?.show()

            textLive?.setOnClickListener(addLiveClickListener(profile.liveplaychannel.liveplaychannellink.applink))
            textLive?.setOnClickListener(addLiveClickListener(profile.liveplaychannel.liveplaychannellink.applink))
            imgProfile?.setOnClickListener(addLiveClickListener(profile.liveplaychannel.liveplaychannellink.applink))
        } else {
            viewLiveRing?.visibility = View.INVISIBLE
            textLive?.hide()

            textLive?.setOnClickListener(null)
            textLive?.setOnClickListener(null)
            imgProfile?.setOnClickListener(null)
        }
    }


    private fun setHeader() {
        val header = view?.findViewById<HeaderUnify>(R.id.header_profile)
        header?.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            addRightIcon(R.drawable.ic_arrow_down).setOnClickListener {
                Toast.makeText(this.context, "arrow down ", Toast.LENGTH_SHORT).show()
            }

            addRightIcon(R.drawable.ic_af_check_gray).setOnClickListener {
                Toast.makeText(this.context, "arrow grey ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoader() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initInjector() {
        DaggerUserProfileComponent.builder()
            .userProfileModule(UserProfileModule(requireContext().applicationContext))
            .build()
            .inject(this)
    }

    override fun onClick(source: View) {
        when (source.id) {
            R.id.text_following_count, R.id.text_following_label -> {
                startActivity(activity?.let {
                    FollowerFollowingListingActivity.getCallingIntent(
                        it,
                        getFollowersBundle(false)
                    )
                })
            }

            R.id.text_follower_count, R.id.text_follower_label -> {
                startActivity(activity?.let {
                    FollowerFollowingListingActivity.getCallingIntent(
                        it,
                        getFollowersBundle(true)
                    )
                })
            }

            R.id.text_see_more -> {
                val textBio = view?.findViewById<TextView>(R.id.text_bio)
                val btnSeeAll = view?.findViewById<TextView>(R.id.text_see_more)
                textBio?.maxLines = 10
                btnSeeAll?.hide()
            }
        }
    }

    private fun getFollowersBundle(isFollowers: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString(EXTRA_DISPLAY_NAME, displayName)
        bundle.putString(EXTRA_USER_NAME, userName)
        bundle.putString(EXTRA_TOTAL_FOLLOWINGS, totalFollowings)
        bundle.putString(EXTRA_TOTAL_FOLLOWERS, totalFollowers)
        bundle.putBoolean(EXTRA_IS_FOLLOWERS, isFollowers)
        return bundle
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun onEmptyList(rawObject: Any?) {
        // TODO("Not yet implemented")
    }

    override fun onStartFirstPageLoad() {
        //  TODO("Not yet implemented")
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        //  TODO("Not yet implemented")
    }

    override fun onStartPageLoad(pageNumber: Int) {
        // TODO("Not yet implemented")
    }

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        //  TODO("Not yet implemented")
    }

    override fun onError(pageNumber: Int) {
        // TODO("Not yet implemented")
    }

    companion object {
        const val VAL_FEEDS_PROFILE = "feeds-profile"
        const val VAL_SOURCE_BUYER = "buyer"
        const val EXTRA_DISPLAY_NAME = "display_name"
        const val EXTRA_TOTAL_FOLLOWERS = "total_followers"
        const val EXTRA_TOTAL_FOLLOWINGS = "total_following"
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_IS_FOLLOWERS = "is_followers"

        fun newInstance(extras: Bundle): Fragment {
            val fragment = UserProfileFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}

