package com.tokopedia.autocompletecomponent.unify.compose_component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.autocompletecomponent.unify.AutoCompleteUnifyDataView
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
internal fun AutoCompleteTitleComponent(item: AutoCompleteUnifyDataView, onItemAction: (item: SuggestionUnify) -> Unit) {
    val domainItem = item.domainModel

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        NestTypography(
            text = domainItem.title.text,
            textStyle = NestTheme.typography.heading4,
            modifier = Modifier
                .padding(vertical = 2.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
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
