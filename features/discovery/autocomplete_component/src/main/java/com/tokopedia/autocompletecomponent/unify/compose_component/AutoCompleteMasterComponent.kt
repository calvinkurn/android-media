package com.tokopedia.autocompletecomponent.unify.compose_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.autocompletecomponent.unify.AutoCompleteUnifyDataView
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify

@Composable
internal fun AutoCompleteMasterComponent(
    item: AutoCompleteUnifyDataView,
    onItemClicked: (item: AutoCompleteUnifyDataView) -> Unit,
    onItemAction: (item: SuggestionUnify) -> Unit
) {
    val domainItem = item.domainModel
    Row(
        modifier = Modifier
            .clickable {
                onItemClicked(item)
            }
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (domainItem.image.imageUrl.isNotBlank()) {
            AutoCompleteLeftIcon(domainItem.image)
        }

        Spacer(Modifier.width(8.dp))

        AutoCompleteDescription(
            domainItem.title,
            item.searchTerm,
            domainItem.isAds,
            domainItem.subtitle,
            Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))
        if (domainItem.cta.imageUrl.isNotBlank()) {
            AutoCompleteRightIconCta(domainItem.cta) {
                onItemAction(item.domainModel)
            }
        } else if (domainItem.label.text.isNotBlank()) {
            AutoCompleteRightLabel(domainItem.label) {
                onItemAction(item.domainModel)
            }
        }
    }
}
