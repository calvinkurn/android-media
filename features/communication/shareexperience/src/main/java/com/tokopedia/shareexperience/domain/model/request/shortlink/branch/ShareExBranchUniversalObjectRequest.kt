package com.tokopedia.shareexperience.domain.model.request.shortlink.branch

import io.branch.indexing.BranchUniversalObject

data class ShareExBranchUniversalObjectRequest(
    /**
     * It's used to uniquely identify the content across the Branch platform
     * Ex in PDP: product id
     */
    val canonicalId: String = "",
    /**
     * Sets the title of the content
     * This is a human-readable name and could be used, for example, in the display of a shared link
     * Ex in PDP: product name
     */
    val title: String = "",
    /**
     * This adds a description for the content. It's a brief summary or explanation of the content
     * Ex in PDP: product name
     */
    val description: String = "",
    /**
     * This sets the URL of an image that represents the content
     * Ex in PDP : product image url
     */
    val contentImageUrl: String = "",
    /**
     * Determines how the content is indexed
     * Setting it to PUBLIC means the content can be discovered publicly, like in search engines or Branch's own content discovery mechanisms
     */
    val contentIndexingMode: BranchUniversalObject.CONTENT_INDEX_MODE = BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC,
    /**
     * ContentMetadata is used to add additional, custom information about the content
     * In this case, a custom key-value pair ("key1", "value1") is being added
     * Ex: {an_min_version, 3.150}
     */
    val contentMetadataMap: Map<String, String> = mapOf()
)
