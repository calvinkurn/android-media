import com.tokopedia.top_ads_headline.view.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    AdDetailsViewModelTest::class,
    AdScheduleAndBudgetViewModelTest::class,
    TopAdsProductListViewModelTest::class,
    EditAdOthersViewModelTest::class,
    EditFormHeadlineViewModelTest::class,
    HeadlineEditKeywordViewModelTest::class,
    SharedEditHeadlineViewModelTest::class,
    TopAdsHeadlineKeyViewModelTest::class
)
class TopAdsHeadlineSuite