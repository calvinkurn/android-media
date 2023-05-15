import com.tokopedia.graphql.coroutines.data.GraphqlInteractor

import com.tokopedia.common.UniversalShareFragmentTest
import com.tokopedia.product.share.ProductShare
import com.tokopedia.product.share.ProductData
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.model.PersonalizedCampaignModel
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput

class PdpShareTest : UniversalShareFragmentTest() {
    private val productShare = ProductShare(requireActivity())
    private val repo = GraphqlInteractor.getInstance().graphqlRepository

    private val mockProductData = ProductData(
        productName = "PDP Product"
    )

    private val affiliateInput = AffiliatePDPInput(

    )

    override fun showUniversalShare(affiliatePDPInput: AffiliatePDPInput) {
        bottomSheet = UniversalShareBottomSheet().apply {
            enableAffiliateCommission(AffiliatePDPInput())
        }
    }

    override fun getAffiliateInput(): AffiliatePDPInput {
        AffiliatePDPInput().apply {

        }
    }
}
