package com.tokopedia.shareexperience.ui

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.ui.ShareExBottomSheetAction
import com.tokopedia.shareexperience.ui.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel
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
) : BaseViewModel(dispatchers.main) {

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
        dummyList.add(ShareExSubtitleUiModel("Mau kasih liat foto yang mana?"))

        val chip = ShareExChipsUiModel(
            listOf(
                ShareExChipUiModel("Chip 1", true),
                ShareExChipUiModel("Chip Chip 2", false),
                ShareExChipUiModel("Chip 3", false),
                ShareExChipUiModel("Chip Chip 4", false),
                ShareExChipUiModel("Chip 5", false),
                ShareExChipUiModel("Chip Chip Chip 6", false)
            )
        )
        dummyList.add(chip)

        val image = ShareExImageCarouselUiModel(
            listOf(
                ShareExImageUiModel(
                    "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                    true
                ),
                ShareExImageUiModel(
                    "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/10/18/999822bb-f444-4602-9203-1643d3f2393a.jpg",
                    false
                ),
                ShareExImageUiModel(
                    "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/7/13/a0a46e5a-c3de-4c73-b25f-35d8a52dafc3.jpg",
                    false
                ),
                ShareExImageUiModel(
                    "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/5/31/82f8d445-615a-49d0-a567-bc9ab6f75195.png",
                    false
                ),
                ShareExImageUiModel(
                    "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/7/6/69b75de5-b8b1-4d3f-9b10-3a1db5e1913a.jpg",
                    false
                )
            )
        )
        dummyList.add(image)

        val shareLinkCard = ShareExLinkShareUiModel(
            "Jual Xiaomi Smart TV Android 10 M...",
            "<b>Komisi Rp50.000</b> / barang dijual",
            "tokopedia.link",
            "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/7/6/69b75de5-b8b1-4d3f-9b10-3a1db5e1913a.jpg",
            "Komisi Extra",
            "Hingga 31 Des 2024"
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
                    ShareExChannelItemModel("IG Feed", IconUnify.INSTAGRAM)
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
                    ShareExChannelItemModel("Lainnya", IconUnify.MENU_KEBAB_HORIZONTAL)
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
