package com.tokopedia.createpost.uprofile.views

import PostItemDecoration
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.ErrorMessage
import com.tokopedia.createpost.uprofile.Loading
import com.tokopedia.createpost.uprofile.Success
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
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.math.abs


class UserProfileFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback,
    ShareBottomsheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var landedUserName: String? = null
    var isFollowed: Boolean = false
    var displayName: String = ""
    var userName: String = ""
    var profileUserId: String = ""
    var profileImage: String = ""
    var totalFollowings: String = ""
    var totalFollowers: String = ""
    var userSession: UserSessionInterface? = null
    var btnAction: UnifyButton? = null
    var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var recyclerviewPost : RecyclerView ? = null
    private var headerProfile : HeaderUnify ? = null
    private var appBarLayout : AppBarLayout? = null
    private var userId = ""

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

        initLandingPageData()
        userSession = UserSession(context)
    }

    private fun initLandingPageData() {
        landedUserName = requireArguments().getString(EXTRA_USERNAME)
        landedUserName?.let {
            mPresenter.getUserDetails(it)
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
        recyclerviewPost = view?.findViewById(R.id.recycler_view)
        recyclerviewPost?.layoutManager = GridLayoutManager(activity, 2)
        if (recyclerviewPost?.itemDecorationCount == 0) {
            recyclerviewPost?.addItemDecoration(
                PostItemDecoration(
                    convertDpToPixel(
                        8F,
                        requireContext()
                    )
                )
            )
        }
        recyclerviewPost?.adapter = mAdapter
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
                        mPresenter.getFollowingStatus(mutableListOf(profileUserId))
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
        if (userSession?.isLoggedIn == false) {
            startActivityForResult(
                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
            return@OnClickListener
        }

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
        userId = data.profileHeader.profile.userID
        initUserPost(userId)

        val textBio = view?.findViewById<TextView>(R.id.text_bio)
        val textUserName = view?.findViewById<TextView>(R.id.text_user_name)
        val textDisplayName = view?.findViewById<TextView>(R.id.text_display_name)
        val textContentCount = view?.findViewById<TextView>(R.id.text_content_count)
        val textFollowerCount = view?.findViewById<TextView>(R.id.text_follower_count)
        val textFollowingCount = view?.findViewById<TextView>(R.id.text_following_count)
        btnAction = view?.findViewById<UnifyButton>(R.id.btn_action_follow)
        appBarLayout = view?.findViewById(R.id.app_bar_layout)

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
        profileImage = data.profileHeader.profile.imageCover

        if (userSession?.isLoggedIn == false) {
            updateToUnFollowUi()
            btnAction?.setOnClickListener {
                startActivityForResult(
                    RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN
                )
            }
        }

        appBarLayout?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) >   convertDpToPixel(OFFSET_USERINFO,requireContext())) {
                headerProfile?.title = data.profileHeader.profile.name
                headerProfile?.subtitle = data.profileHeader.profile.username
            } else {
                headerProfile?.title = ""
                headerProfile?.subtitle = ""
            }
        })
    }

    private fun setActionButton(followProfile: UserProfileIsFollow) {

        if (!userSession?.userId.isNullOrBlank() && followProfile.profileHeader.items[0].userID == userSession?.userId) {
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
        headerProfile = view?.findViewById<HeaderUnify>(R.id.header_profile)
        headerProfile?.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            val imgShare = addRightIcon(R.drawable.iconunify_share_mobile)

            imgShare.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            imgShare.setOnClickListener {
                showUniversalShareBottomSheet()
            }

            val imgMenu = addRightIcon(R.drawable.iconunify_menu_hamburger)

            imgMenu.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            imgMenu.setOnClickListener {
                RouteManager.route(activity, "tokopedia://navigation/main")
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

    /**
     * OFFSET_USERINFO = 64dp(Profile) + 10dp(PaddingTop) + 16dp (margin top username) + 2dp (margin top userid) +
     *  24dp(user name line height) + 20dp(userid line height)
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            initLandingPageData()
        }
    }

    private fun showUniversalShareBottomSheet() {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@UserProfileFragment)
            userSession?.userId?.ifEmpty { "0" }?.let {
                setUtmCampaignData(
                    "Profile",
                    it,
                    userName,
                    "share"
                )
            }
            setMetaData(
                tnTitle = displayName,
                tnImage = profileImage)
            setOgImageUrl(profileImage ?: "")
            imageSaved(profileImage)
        }
        universalShareBottomSheet?.show(fragmentManager, this)
    }

    /*override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.SHOP_TYPE
            uri = shopPageHeaderDataModel?.shopCoreUrl
            id = shopPageHeaderDataModel?.shopId
            //set and share in the Linker Data
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            ogTitle = getShareBottomSheetOgTitle()
            ogDescription = getShareBottomSheetOgDescription()
            if(shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        })
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    context?.let{
                        checkUsingCustomBranchLinkDomain(linkerShareData)
                        var shareString = getString(
                            androidx.lifecycle.R.string.shop_page_share_text_with_link,
                            shopPageHeaderDataModel?.shopName,
                            linkerShareData?.shareContents
                        )
                        shareModel.subjectName = shopPageHeaderDataModel?.shopName.toString()
                        SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareString)
                        // send gql tracker
                        shareModel.socialMediaName?.let { name ->
                            shopViewModel?.sendShopShareTracker(
                                shopId,
                                channel = when (shareModel) {
                                    is ShareModel.CopyLink -> {
                                        ShopPageConstant.SHOP_SHARE_DEFAULT_CHANNEL
                                    }
                                    is ShareModel.Others -> {
                                        ShopPageConstant.SHOP_SHARE_OTHERS_CHANNEL
                                    }
                                    else -> name
                                }
                            )
                        }

                        // send gtm tracker
                        if(isGeneralShareBottomSheet) {
                            shopPageTracking?.clickShareBottomSheetOption(
                                shareModel.channel.orEmpty(),
                                customDimensionShopPage,
                                userId
                            )
                            if(!isMyShop) {
                                shopPageTracking?.clickGlobalHeaderShareBottomSheetOption(
                                    shareModel.channel.orEmpty(),
                                    customDimensionShopPage,
                                    userId
                                )
                            }
                        } else{
                            shopPageTracking?.clickScreenshotShareBottomSheetOption(
                                shareModel.channel.orEmpty(),
                                customDimensionShopPage,
                                userId
                            )
                        }

                        //we have to check if we can move it inside the common function
                        universalShareBottomSheet?.dismiss()
                    }
                }

                override fun onError(linkerError: LinkerError?) {}
            })
        )
    }

    private fun getShareBottomSheetOgTitle(): String {
        return shopPageHeaderDataModel?.let{
            "${joinStringWithDelimiter(it.shopName, it.location, delimiter = " - ")} | Tokopedia"
        } ?: ""
    }

    private fun getShareBottomSheetOgDescription(): String {
        return shopPageHeaderDataModel?.let{
            joinStringWithDelimiter(it.description, it.tagline, delimiter = " - ")
        } ?: ""
    }

    override fun onCloseOptionClicked() {
        if (isUsingNewShareBottomSheet(requireContext())) {
            if(isGeneralShareBottomSheet)
                shopPageTracking?.clickCloseNewShareBottomSheet(customDimensionShopPage, userId)
            else
                shopPageTracking?.clickCloseNewScreenshotShareBottomSheet(customDimensionShopPage, userId)
        } else {
            shopPageTracking?.clickCancelShareBottomsheet(customDimensionShopPage, isMyShop)
        }
    } */

    companion object {
        const val VAL_FEEDS_PROFILE = "feeds-profile"
        const val VAL_SOURCE_BUYER = "buyer"
        const val EXTRA_DISPLAY_NAME = "display_name"
        const val EXTRA_TOTAL_FOLLOWERS = "total_followers"
        const val EXTRA_TOTAL_FOLLOWINGS = "total_following"
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_IS_FOLLOWERS = "is_followers"
        const val OFFSET_USERINFO = 136F
        const val REQUEST_CODE_LOGIN = 1

        fun newInstance(extras: Bundle): Fragment {
            val fragment = UserProfileFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        TODO("Not yet implemented")
    }

    override fun onCloseOptionClicked() {
        TODO("Not yet implemented")
    }
}

