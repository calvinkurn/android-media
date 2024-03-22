package com.tokopedia.stories.widget.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.compose.NestCheckbox
import com.tokopedia.unifycomponents.compose.NestSwitch

/**
 * @author by astidhiyaa on 3/22/24
 */
@Composable
internal fun StoriesSettingsScreen() {
    NestTheme(isOverrideStatusBarColor = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            NestTypography(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Story",
                textStyle = NestTheme.typography.display1.copy(fontWeight = FontWeight.Bold),
            )
            Row {
                NestIcon(iconId = IconUnify.SOCIAL_STORY, modifier = Modifier.padding(end = 12.dp))
                Column {
                    NestTypography(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = "Buat Story Otomatis",
                        textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold),
                    )
                    NestTypography(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = "Story akan otomatis dibuat setiap ada update produk di tokomu. Selengkapnya",
                        textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600),
                    )
                    NestTypography(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = "Kategori update produk yang akan jadi Story:",
                        textStyle = NestTheme.typography.paragraph3,
                    )

                    LazyColumn {
                        items(settingOpts) { item ->
                            SettingOptItem(item)
                        }
                    }
                }
                NestSwitch(isChecked = false, onCheckedChanged = {})
            }
        }
    }
}

@Composable
private fun SettingOptItem(item: StoriesSettingOpt) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NestTypography(
            text = item.text,
            textStyle = NestTheme.typography.paragraph3,
        )
        NestCheckbox(isChecked = item.isSelected, onCheckedChange = {})
    }
}

@Preview
@Composable
internal fun SettingScreen() {
    StoriesSettingsScreen()
}

data class StoriesSettingOpt(
    val text: String,
    val isSelected: Boolean,
) //TODO: need to wait for BE for contract

val settingOpts = buildList<StoriesSettingOpt> {
    add(StoriesSettingOpt("Flash Sale", false))
    add(StoriesSettingOpt("Rilisan Spesial", false))
    add(StoriesSettingOpt("Restok", false))
    add(StoriesSettingOpt("Diskon", false))
    add(StoriesSettingOpt("Produk Baru", false))
}
