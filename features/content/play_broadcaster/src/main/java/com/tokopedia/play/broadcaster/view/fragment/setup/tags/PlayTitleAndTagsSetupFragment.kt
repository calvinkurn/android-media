package com.tokopedia.play.broadcaster.view.fragment.setup.tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.view.custom.PlayBottomSheetHeader
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.TagAddedListViewComponent
import com.tokopedia.play.broadcaster.view.partial.TagRecommendationListViewComponent
import com.tokopedia.play.broadcaster.view.partial.TextFieldAddTagViewComponent
import com.tokopedia.play.broadcaster.view.partial.TextFieldTitleViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayTitleAndTagsSetupViewModel
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.viewcomponent.viewComponent
import javax.inject.Inject

/**
 * Created by jegul on 17/02/21
 */
class PlayTitleAndTagsSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatcherProvider,
) : PlayBaseSetupFragment(), TagAddedListViewComponent.Listener, TextFieldAddTagViewComponent.Listener {

    private var mListener: Listener? = null

    private lateinit var viewModel: PlayTitleAndTagsSetupViewModel

    /**
     * UI
     */
    private lateinit var bottomSheetHeader: PlayBottomSheetHeader
    private val titleFieldView by viewComponent(isEagerInit = true) { TextFieldTitleViewComponent(it, R.id.text_field_title) }
    private val tagRecommendationListView by viewComponent { TagRecommendationListViewComponent(it, R.id.rv_tags_recommendation) }
    private val titleFieldAddTag by viewComponent(isEagerInit = true) { TextFieldAddTagViewComponent(it, R.id.text_field_tag, this) }
    private val tagAddedListView by viewComponent { TagAddedListViewComponent(it, R.id.rv_tags_added, this) }

    override fun onInterceptBackPressed(): Boolean {
        return false
    }

    override fun getScreenName(): String {
        return "Title and Tags Setup"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(PlayTitleAndTagsSetupViewModel::class.java)
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
     * Tag Added List View Component
     */
    override fun onTagRemoved(view: TagAddedListViewComponent, tag: String) {
        viewModel.removeTag(tag)
    }

    /**
     * Text Field Add Tag View Component
     */
    override fun onTagEntered(view: TextFieldAddTagViewComponent, tag: String) {
        viewModel.addTag(tag)
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
    }

    private fun setupObserve() {
        observeRecommendedTags()
        observeAddedTags()
    }

    private fun observeRecommendedTags() {
        viewModel.observableRecommendedTags.observe(viewLifecycleOwner, Observer {
            tagRecommendationListView.setTags(it.toList())
        })
    }

    private fun observeAddedTags() {
        viewModel.observableAddedTags.observe(viewLifecycleOwner, Observer {
            tagAddedListView.setTags(it.toList())
        })
    }

    interface Listener {

        suspend fun onTitleAndTagsSetupFinished(dataStore: PlayBroadcastSetupDataStore): Throwable?
    }
}