package com.tokopedia.createpost.uprofile.views

import PostItemDecoration
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
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
import com.tokopedia.createpost.uprofile.viewmodels.UserProfileViewModel
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.feedcomponent.util.util.convertDpToPixel
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.up_layout_user_profile_header.*
import javax.inject.Inject


class UserProfileFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var landedUserName: String? = null

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
            initUserPost()
            mPresenter.getUserDetails(it)
            mPresenter.getUPlayVideos(VAL_FEEDS_PROFILE, "", VAL_SOURCE_BUYER, it)
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

    private fun initUserPost() {
        val postRv = view?.findViewById<RecyclerView>(R.id.recycler_view)
        postRv?.layoutManager = GridLayoutManager(activity, 2)
        postRv?.addItemDecoration(PostItemDecoration(convertDpToPixel(8F, requireContext())))
        postRv?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading()
    }

    private fun initObserver() {
        addUserProfileObserver()
        addListObserver()
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

    private fun addLiveClickListener(appLink: String) = View.OnClickListener {
        RouteManager.route(context, appLink)
    }


    private fun setMainUi(data: ProfileHeaderBase) {
        val textBio = view?.findViewById<TextView>(R.id.text_bio)
        val textUserName = view?.findViewById<TextView>(R.id.text_user_name)
        val textDisplayName = view?.findViewById<TextView>(R.id.text_display_name)
        val textContentCount = view?.findViewById<TextView>(R.id.text_content_count)
        val textFollowerCount = view?.findViewById<TextView>(R.id.text_follower_count)
        val textFollowingCount = view?.findViewById<TextView>(R.id.text_following_count)
        val btnActionFollow = view?.findViewById<UnifyButton>(R.id.btn_action_follow)

        textBio?.text = data.profileHeader.profile.biography
        textUserName?.text = data.profileHeader.profile.username
        textDisplayName?.text = data.profileHeader.profile.name
        textContentCount?.text = data.profileHeader.stats.totalPostFmt
        textFollowerCount?.text = data.profileHeader.stats.totalFollowerFmt
        textFollowingCount?.text = data.profileHeader.stats.totalFollowingFmt

        setProfileImg(data.profileHeader.profile)
    }

    private fun setProfileImg(profile: Profile) {
        if (profile == null
            || profile.liveplaychannel == null
            || !URLUtil.isValidUrl(profile.imageCover)
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
            viewLiveRing?.hide()
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
                startActivity(Intent(activity, FollowerFollowingListingActivity::class.java))
            }

            R.id.text_follower_count, R.id.text_follower_label -> {
                startActivity(Intent(activity, FollowerFollowingListingActivity::class.java))
            }

            R.id.text_live, R.id.img_profile_image, R.id.view_profile_outer_ring -> {
                Toast.makeText(context, "Profile image", Toast.LENGTH_SHORT).show()
            }

            R.id.text_see_more -> {
                Toast.makeText(context, "See All", Toast.LENGTH_SHORT).show()
            }

            R.id.btn_action_follow -> {
                Toast.makeText(context, "Follow/Unfollow", Toast.LENGTH_SHORT).show()
            }
        }
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
        const val EXTRA_USERNAME = "userName"
        const val VAL_FEEDS_PROFILE = "feeds-profile"
        const val VAL_SOURCE_BUYER = "buyer"

        fun newInstance(extras: Bundle): Fragment {
            val fragment = UserProfileFragment()
            fragment.arguments = extras
            return fragment
        }
    }
}

