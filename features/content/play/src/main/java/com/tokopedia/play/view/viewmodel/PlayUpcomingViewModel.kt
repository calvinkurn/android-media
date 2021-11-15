package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play_common.sse.PlayChannelSSE
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
class PlayUpcomingViewModel @Inject constructor(
    private val getChannelStatusUseCase: GetChannelStatusUseCase,
    private val playChannelSSE: PlayChannelSSE,
    private val repo: PlayViewerRepository,
): ViewModel() {

}