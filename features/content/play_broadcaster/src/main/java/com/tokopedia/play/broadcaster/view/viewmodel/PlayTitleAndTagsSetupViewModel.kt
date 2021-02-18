package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import javax.inject.Inject

/**
 * Created by jegul on 17/02/21
 */
class PlayTitleAndTagsSetupViewModel @Inject constructor() : ViewModel() {

    val observableAddedTags: LiveData<List<String>>
        get() = Transformations.map(_observableAddedTags) { it.toList() }
    val observableRecommendedTags: LiveData<List<String>>
        get() = Transformations.map(_observableRecommendedTags) { it.toList() }

    private val _observableAddedTags = MutableLiveData<Set<String>>()
    private val _observableRecommendedTags = MediatorLiveData<Set<String>>().apply {
        addSource(_observableAddedTags) {
            value = recommendedTags - it
        }
    }

    private val recommendedTags = getRecommendedTags()
    private val addedTags = mutableSetOf<String>()

    init {
        addedTags.addAll(getAddedTags())
        refreshAddedTags()
    }

    fun addTag(tag: String) {
        addedTags.add(tag)
        refreshAddedTags()
    }

    fun removeTag(tag: String) {
        addedTags.remove(tag)
        refreshAddedTags()
    }

    private fun refreshAddedTags() {
        _observableAddedTags.value = addedTags
    }

    /**
     * Mock data
     */
    private fun getRecommendedTags() = setOf(
            "adfb",
            "aaaaaaa",
            "alola alolaergregre",
            "1234",
            "1eberbr",
            "aergreg"
    )

    private fun getAddedTags() = setOf(
            "adfb",
            "adfb  ergerger  ergeragerg regerg",
            "adfb ergerag",
            "adfb  greqgrqeg",
            "adfb",
    )
}