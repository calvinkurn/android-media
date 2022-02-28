package com.tokopedia.createpost.uprofile.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.uprofile.ErrorMessage
import com.tokopedia.createpost.uprofile.Loading
import com.tokopedia.createpost.uprofile.Success
import com.tokopedia.createpost.uprofile.di.DaggerUserProfileComponent
import com.tokopedia.createpost.uprofile.di.UserProfileModule
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.viewmodels.FollowerFollowingViewModel
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.user.session.UserSession
import javax.inject.Inject


class FollowingListingFragment : BaseDaggerFragment(), View.OnClickListener, AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val userSessionInterface: UserSession by lazy {
        UserSession(context)
    }

    private val mPresenter: FollowerFollowingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FollowerFollowingViewModel::class.java)
    }


    private val mAdapter: ProfileFollowingAdapter by lazy {
        ProfileFollowingAdapter(
            mPresenter,
            this,
            userSessionInterface,
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
        return inflater.inflate(R.layout.up_fragment_psger_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        //initListener()
        val userSessionInterface = UserSession(context)
        //mPresenter.getUserDetails(userSessionInterface.userId)
        initMainUi()
    }

    private fun initObserver() {
        addListObserver()
    }

    private fun initMainUi() {
        val rvFollowers = view?.findViewById<RecyclerView>(R.id.rv_followers)
        rvFollowers?.adapter = mAdapter
        mAdapter.resetAdapter()
        mAdapter.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_NAME))
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

    private fun addListObserver() =
        mPresenter.profileFollowingsListLiveData.observe(viewLifecycleOwner, Observer {
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

    private fun setMainUi(data: ProfileHeaderBase) {

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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UserProfileFragment.REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            refreshPage()
        }
    }

    fun refreshPage() {
        mAdapter?.resetAdapter()
        mAdapter?.startDataLoading(arguments?.getString(UserProfileFragment.EXTRA_USER_NAME, ""))
    }

    companion object {
        fun newInstance(extras: Bundle): Fragment {
            val fragment = FollowingListingFragment()
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

