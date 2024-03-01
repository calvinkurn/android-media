package com.tokopedia.shareexperience.stub

import android.content.Context
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.BranchError.ERR_BRANCH_KEY_INVALID
import io.branch.referral.util.LinkProperties
import javax.inject.Inject

class ShareExBranchUniversalObjectStub @Inject constructor() : BranchUniversalObject() {

    var isError = false
    var url = "https://tokopedia.link/test123"

    override fun generateShortUrl(
        context: Context,
        linkProperties: LinkProperties,
        callback: Branch.BranchLinkCreateListener?
    ) {
        if (!isError) {
            callback?.onLinkCreate(
                url,
                null
            )
        } else {
            callback?.onLinkCreate(
                "",
                BranchError("Oops!", ERR_BRANCH_KEY_INVALID)
            )
        }
    }

    override fun generateShortUrl(
        context: Context,
        linkProperties: LinkProperties,
        callback: Branch.BranchLinkCreateListener?,
        defaultToLongUrl: Boolean
    ) {
        if (!isError) {
            callback?.onLinkCreate(
                url,
                null
            )
        } else {
            callback?.onLinkCreate(
                "",
                BranchError("Oops!", ERR_BRANCH_KEY_INVALID)
            )
        }
    }
}
