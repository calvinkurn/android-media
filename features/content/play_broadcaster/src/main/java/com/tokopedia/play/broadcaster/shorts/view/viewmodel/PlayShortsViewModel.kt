package com.tokopedia.play.broadcaster.shorts.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsViewModel @Inject constructor(
    private val repo: PlayShortsRepository,
    private val sharedPref: HydraSharedPreferences,
) : ViewModel() {

    private val _mediaUri = MutableStateFlow("")
    private val _accountList = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    private val _selectedAccount = MutableStateFlow<ContentAccountUiModel>(ContentAccountUiModel.Empty)
    private val _title = MutableStateFlow<String>("")
    private val _shortsId = MutableStateFlow<String>("")

    val uiState: Flow<PlayShortsUiState> = combine(
        _shortsId,
        _mediaUri,
        _accountList,
        _selectedAccount,
    ) { shortsId, mediaUri, accountList, selectedAccount ->
        PlayShortsUiState(
            shortsId = shortsId,
            mediaUri = mediaUri,
            accountList = accountList,
            selectedAccount = selectedAccount
        )
    }

    /**
     * shortsId != null && mediaUri == null -> MediaPicker
     * shortsId != null && mediaUri != null -> Preparation
     */
}
