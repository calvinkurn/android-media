import com.tokopedia.topads.view.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        AdChooserViewModelTest::class,
        BudgetingAdsViewModelTest::class,
        CreateGroupAdsViewModelTest::class,
        KeywordAdsViewModelTest::class,
        ProductAdsListViewModelTest::class,
        SummaryViewModelTest::class
)
class AutoAdsCreateSuite