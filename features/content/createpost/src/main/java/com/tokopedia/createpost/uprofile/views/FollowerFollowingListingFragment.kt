package com.tokopedia.createpost.uprofile.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.*
import com.tokopedia.createpost.uprofile.di.DaggerUserProfileComponent
import com.tokopedia.createpost.uprofile.di.UserProfileModule
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.viewmodels.FollowerFollowingViewModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import javax.inject.Inject


class FollowerFollowingListingFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mPresenter: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }

//    private val mAdapter: UserPostBaseAdapter by lazy {
//        UserPostBaseAdapter(
//            mPresenter,
//            this
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initInjector()
        return inflater.inflate(R.layout.up_fragment_follower_following_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initObserver()
        //initListener()
        val userSessionInterface = UserSession(context)
        //mPresenter.getUserDetails(userSessionInterface.userId)
        //initUserPost()
    }

    private fun initObserver() {
        //observeUserProfile()
    }

    private fun initListener() {
//        view?.findViewById<Group>(R.id.gr_following)?.setOnClickListener(this)
//        view?.findViewById<Group>(R.id.gr_follower)?.setOnClickListener(this)
    }

    private fun initUserPost() {
//        val postRv = view?.findViewById<RecyclerView>(R.id.recycler_view)
//        postRv?.layoutManager = GridLayoutManager(activity, 2)
//        postRv?.adapter = mAdapter
//        mAdapter.resetAdapter()
//        mAdapter.startDataLoading()
    }

//    private fun observeUserProfile() =
//        mPresenter.userDetailsLiveData.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                when (it) {
//                    is Loading -> showLoader()
//                    is ErrorMessage -> {
//
//                    }
//                    is Success -> {
//                        setMainUi(it.data)
//                    }
//                }
//            }
//        })

    private fun setMainUi(data: ProfileHeaderBase) {
        val textBio = view?.findViewById<TextView>(R.id.text_bio)
        val textUserName = view?.findViewById<TextView>(R.id.text_user_name)
        val textDisplayName = view?.findViewById<TextView>(R.id.text_display_name)
        val textContentCount = view?.findViewById<TextView>(R.id.text_content_count)
        val textFollowerCount = view?.findViewById<TextView>(R.id.text_follower_count)
        val textFollowingCount = view?.findViewById<TextView>(R.id.text_following_count)
        val textLive = view?.findViewById<TextView>(R.id.text_live)
        val btnActionFollow = view?.findViewById<UnifyButton>(R.id.btn_action_follow)
        val viewLiveRing = view?.findViewById<View>(R.id.view_profile_outer_ring)
        val imgProfile = view?.findViewById<ImageUnify>(R.id.img_profile_image)

        textBio?.text = data.profileHeader.profile.biography
        textUserName?.text = data.profileHeader.profile.username
        textDisplayName?.text = data.profileHeader.profile.name
        textContentCount?.text = data.profileHeader.stats.totalPostFmt
        textFollowerCount?.text = data.profileHeader.stats.totalFollowerFmt
        textFollowingCount?.text = data.profileHeader.stats.totalFollowingFmt

        imgProfile?.urlSrc = data.profileHeader.profile.imageCover

        if (data.profileHeader.profile.liveplaychannel.islive) {
            viewLiveRing?.show()
            textLive?.show()
        } else {
            viewLiveRing?.show()
            textLive?.show()
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
            R.id.gr_follower -> {
                Toast.makeText(context, "Follower", Toast.LENGTH_SHORT).show()
            }
            R.id.gr_following -> {
                Toast.makeText(context, "following", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

        }
    }

    companion object {
        fun newInstance(extras: Bundle): Fragment {
            val fragment = FollowerFollowingListingFragment()
            fragment.arguments = extras
            return fragment
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
}

