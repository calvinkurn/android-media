package com.tokopedia.shareexperience.ui.view

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.domain.model.property.ShareExChipModel
import com.tokopedia.shareexperience.ui.ShareExBottomSheetAction
import com.tokopedia.shareexperience.ui.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.ShareExImageCarouselUiModel
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareExViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _actionFlow =
        MutableSharedFlow<ShareExBottomSheetAction>(extraBufferCapacity = 16)

    private val _bottomSheetUiState = MutableStateFlow(ShareExBottomSheetUiState())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    fun processAction(action: ShareExBottomSheetAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    private fun Flow<ShareExBottomSheetAction>.process() {
        onEach {
            when (it) {
                is ShareExBottomSheetAction.InitializePage -> {
                    setDummyData()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun setDummyData() {
        val dummyList = arrayListOf<Visitable<in ShareExTypeFactory>>()
        dummyList.add(ShareExSubtitleUiModel("Mau kasih liat foto yang mana"))

        val chip = ShareExChipsUiModel(
            listOf(ShareExChipModel("Chip 1"), ShareExChipModel("Chip 2"), ShareExChipModel("Chip 3"))
        )
        dummyList.add(chip)

        val image = ShareExImageCarouselUiModel(
            listOf("https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png", "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png", "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png")
        )
        dummyList.add(image)

        val shareLinkCard = ShareExLinkShareUiModel(
            "Samsung Official Store",
            "Komisi hingga 10%/barang terjual",
            "tokopedia.link",
            "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
            "Komisi Ekstra",
            "Hingga 31 Des 2022"
        )
        dummyList.add(shareLinkCard)

        val separator = ShareExSeparatorUiModel()
        dummyList.add(separator)

        val affiliateRegistration = ShareExAffiliateRegistrationUiModel(
            "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
            "Tokopedia Affiliate",
            "Daftar dan raih komisi Rp16.000/barang terjual dengan bagikan link ini.",
            "BARU"
        )
        dummyList.add(affiliateRegistration)

        val socialChannel = ShareExSocialChannelUiModel(
            ShareExChannelModel(
                "",
                listOf(
                    ShareExChannelItemModel("WhatsApp", IconUnify.WHATSAPP),
                    ShareExChannelItemModel("FB Feed", IconUnify.FACEBOOK),
                    ShareExChannelItemModel("FB Story", IconUnify.FACEBOOK_STORY),
                    ShareExChannelItemModel("IG Feed", IconUnify.INSTAGRAM),
                )
            )
        )
        dummyList.add(socialChannel)

        val commonChannel = ShareExCommonChannelUiModel(
            ShareExChannelModel(
                "",
                listOf(
                    ShareExChannelItemModel("Salin Link", IconUnify.LINK),
                    ShareExChannelItemModel("SMS", IconUnify.CHAT),
                    ShareExChannelItemModel("E-mail", IconUnify.MESSAGE),
                    ShareExChannelItemModel("Lainnya", IconUnify.MENU_KEBAB_HORIZONTAL),
                )
            )
        )
        dummyList.add(commonChannel)

        viewModelScope.launch {
            _bottomSheetUiState.update {
                it.copy(uiModelList = dummyList)
            }
        }
    }
}
