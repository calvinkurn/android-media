import com.tokopedia.common.UniversalShareFragmentTest
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.AffiliateInput

class PdpShareTest : UniversalShareFragmentTest() {
    private val productShare = ProductShare(requireActivity())
    private val repo = GraphqlInteractor.getInstance().graphqlRepository

    private val mockProductData = ProductData(
        productName = "PDP Product"
    )

    private val affiliateInput = AffiliateInput()

    override fun showUniversalShare(affiliateInput: AffiliateInput) {
        bottomSheet = UniversalShareBottomSheet().apply {
            enableAffiliateCommission(AffiliateInput())
        }
    }

    override fun getAffiliateInput(): AffiliateInput {
        AffiliateInput().apply {
        }
    }
}
