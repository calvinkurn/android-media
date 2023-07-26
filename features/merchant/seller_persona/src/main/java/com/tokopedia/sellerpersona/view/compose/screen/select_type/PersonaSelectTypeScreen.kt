package com.tokopedia.sellerpersona.view.compose.screen.select_type

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 25/07/23.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SelectPersonaTypeScreen(data: SelectTypeState, onEvent: (SelectTypeUiEvent) -> Unit) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            when (data.state) {
                is SelectTypeState.State.Loading -> PersonaTypeLoadingState()
                is SelectTypeState.State.Error -> SelectTypeErrorState(onEvent)
                is SelectTypeState.State.Success -> PersonSuccessState(data.data, onEvent)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    NestTheme(darkTheme = false) {
        SelectPersonaTypeScreen(
            data = SelectTypeState(
                state = SelectTypeState.State.Error(Exception()),
                data = SelectTypeState.Data(
                    personaList = listOf(
                        PersonaUiModel(
                            value = "corporate-supervisor-owner",
                            headerTitle = "Gedongan",
                            headerSubTitle = "Pemilik Toko",
                            avatarImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_gedongan-min.png",
                            backgroundImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_gedongan-min.png",
                            bodyTitle = "Pilih tipe ini jika kamu:",
                            itemList = listOf(
                                "Menerima 1-10 pesanan per hari",
                                "Punya toko fisik (offline)",
                                "Punya pegawai yang mengurus operasional toko",
                                "Sering mencari peluang untuk strategi baru"
                            ),
                            isSelected = true
                        ), PersonaUiModel(
                            value = "corporate-supervisor-owner",
                            headerTitle = "Gedongan",
                            headerSubTitle = "Pemilik Toko",
                            avatarImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_gedongan-min.png",
                            backgroundImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_gedongan-min.png",
                            bodyTitle = "Pilih tipe ini jika kamu:",
                            itemList = listOf(
                                "Menerima 1-10 pesanan per hari",
                                "Punya toko fisik (offline)",
                                "Punya pegawai yang mengurus operasional toko",
                                "Sering mencari peluang untuk strategi baru"
                            )
                        ), PersonaUiModel(
                            value = "corporate-supervisor-owner",
                            headerTitle = "Gedongan",
                            headerSubTitle = "Pemilik Toko",
                            avatarImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_gedongan-min.png",
                            backgroundImage = "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_gedongan-min.png",
                            bodyTitle = "Pilih tipe ini jika kamu:",
                            itemList = listOf(
                                "Menerima 1-10 pesanan per hari",
                                "Punya toko fisik (offline)",
                                "Punya pegawai yang mengurus operasional toko",
                                "Sering mencari peluang untuk strategi baru"
                            )
                        )
                    )
                )
            ), onEvent = {}
        )
    }
}
