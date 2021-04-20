package com.tokopedia.play.broadcaster.view.fragment.setup.tags

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.*
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.*
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayTitleAndTagsSetupViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject

/**
 * Created by jegul on 17/02/21
 */
class PlayTitleAndTagsSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatchers,
) : PlayBaseSetupFragment(),
        TagAddedListViewComponent.Listener,
        TextFieldAddTagViewComponent.Listener,
        BottomActionNextViewComponent.Listener,
        TextFieldTitleViewComponent.Listener,
        TagRecommendationListViewComponent.Listener {

    private var mListener: Listener? = null

    private lateinit var viewModel: PlayTitleAndTagsSetupViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    /**
     * UI
     */
    private lateinit var bottomSheetHeader: PlayBottomSheetHeader
    private val titleFieldView by viewComponent(isEagerInit = true) { TextFieldTitleViewComponent(it, R.id.text_field_title, this) }
    private val tagRecommendationListView by viewComponent { TagRecommendationListViewComponent(it, R.id.rv_tags_recommendation, this) }
    private val bottomActionNextView by viewComponent { BottomActionNextViewComponent(it, this) }

    private var toasterBottomMargin = 0

    override fun onInterceptBackPressed(): Boolean {
        return false
    }

    override fun getScreenName(): String {
        return "Title and Tags Setup"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayTitleAndTagsSetupViewModel::class.java)
        dataStoreViewModel = ViewModelProvider(this, viewModelFactory).get(DataStoreViewModel::class.java)
        setupTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_title_and_tags_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView()
        setupObserve()
    }

    /**
     * Text Field Title View Component
     */
    override fun onTitleInputChanged(view: TextFieldTitleViewComponent, title: String) {
        bottomActionNextView.setEnabled(viewModel.isTitleValid(title))
    }

    /**
     * Tag Recommendation List View Component
     */
    override fun onTagClicked(view: TagRecommendationListViewComponent, tag: String) {
        viewModel.toggleTag(tag)
    }

    /**
     * Tag Added List View Component
     */
    override fun onTagRemoved(view: TagAddedListViewComponent, tag: String) {
        viewModel.removeTag(tag)
    }

    /**
     * Text Field Add Tag View Component
     */
    override fun onTagSubmitted(view: TextFieldAddTagViewComponent, tag: String) {
        viewModel.toggleTag(tag)
    }

    /**
     * Bottom Action Next View Component
     */
    override fun onNextButtonClicked(view: BottomActionNextViewComponent) {
        //Next
        viewModel.finishSetup(titleFieldView.getText())
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun initView(view: View) {
        with(view) {
            bottomSheetHeader = findViewById(R.id.bottom_sheet_header)
        }
    }

    private fun setupView() {
        bottomSheetHeader.setHeader(getString(R.string.play_title_and_tags_title), isRoot = false)

        bottomSheetHeader.setListener(object : PlayBottomSheetHeader.Listener {
            override fun onBackButtonClicked(view: PlayBottomSheetHeader) {
                bottomSheetCoordinator.goBack()
            }
        })

        bottomActionNextView.setEnabled(false)
    }

    private fun setupObserve() {
        observeRecommendedTags()
        observeAddedTags()
        observeUploadEvent()
    }

    private fun observeRecommendedTags() {
        viewModel.observableRecommendedTags.observe(viewLifecycleOwner, Observer {
            tagRecommendationListView.setTags(it.toList())
        })
    }

    private fun observeAddedTags() {
//        viewModel.observableAddedTags.observe(viewLifecycleOwner, Observer {
//            tagAddedListView.setTags(it.toList())
//        })
    }

    private fun observeUploadEvent() {
        viewModel.observableUploadEvent.observe(viewLifecycleOwner, Observer {
            when (val content = it.peekContent()) {
                NetworkResult.Loading -> bottomActionNextView.setLoading(true)
                is NetworkResult.Fail -> onUploadFailed(content.error)
                is NetworkResult.Success -> {
                    if (!it.hasBeenHandled) onUploadSuccess()
                }
            }
        })
    }

    private fun onUploadSuccess() {
        viewLifecycleOwner.lifecycleScope.launch {
            val error = mListener?.onTitleAndTagsSetupFinished(dataStoreViewModel.getDataStore())
            error?.let {
                yield()
                onUploadFailed(it)
            }
        }
    }

    private fun onUploadFailed(e: Throwable?) {
        bottomActionNextView.setLoading(false)
        e?.localizedMessage?.let {
            errMessage -> showToaster(errMessage, type = Toaster.TYPE_ERROR)
        }
    }

    private fun showToaster(message: String, type: Int = Toaster.TYPE_NORMAL, duration: Int = Toaster.LENGTH_SHORT, actionLabel: String = "", actionListener: View.OnClickListener = View.OnClickListener { }) {
        if (toasterBottomMargin == 0) {
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            toasterBottomMargin = bottomActionNextView.rootView.height + offset8
        }

        view?.showToaster(
                message = message,
                type = type,
                duration = duration,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupEnterTransition()
        setupReturnTransition()
    }

    private fun setupEnterTransition() {
        enterTransition = TransitionSet()
                .addTransition(Slide(Gravity.END))
                .addTransition(Fade(Fade.IN))
                .setStartDelay(200)
                .setDuration(300)

        sharedElementEnterTransition = TransitionSet()
                .addTransition(ChangeTransform())
                .addTransition(ChangeBounds())
                .setDuration(450)
    }

    private fun setupReturnTransition() {
        returnTransition = TransitionSet()
                .addTransition(Slide(Gravity.END))
                .addTransition(Fade(Fade.OUT))
                .setDuration(250)

        sharedElementReturnTransition = TransitionSet()
                .addTransition(ChangeTransform())
                .addTransition(ChangeBounds())
                .setDuration(450)
    }

    interface Listener {

        suspend fun onTitleAndTagsSetupFinished(dataStore: PlayBroadcastSetupDataStore): Throwable?
    }
}