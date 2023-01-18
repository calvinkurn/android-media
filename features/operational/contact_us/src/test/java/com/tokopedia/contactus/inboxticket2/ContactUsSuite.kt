package com.tokopedia.contactus.inboxticket2

import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterTest
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterTest
import com.tokopedia.contactus.inboxtickets.usecase.CloseTicketByUserUseCaseTest
import com.tokopedia.contactus.inboxtickets.usecase.GetTicketListUseCaseTest
import com.tokopedia.contactus.inboxtickets.usecase.InboxOptionUseCaseTest
import com.tokopedia.contactus.inboxtickets.usecase.PostMessageUseCase2Test
import com.tokopedia.contactus.inboxtickets.usecase.PostMessageUseCaseTest
import com.tokopedia.contactus.inboxtickets.usecase.SubmitRatingUseCaseTest
import com.tokopedia.contactus.inboxtickets.usecase.UploadImageUseCaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        CloseTicketByUserUseCaseTest::class,
        GetTicketListUseCaseTest::class,
        InboxOptionUseCaseTest::class,
        PostMessageUseCase2Test::class,
        PostMessageUseCaseTest::class,
        SubmitRatingUseCaseTest::class,
        UploadImageUseCaseTest::class,
        InboxDetailPresenterTest::class,
        InboxListPresenterTest::class
)
class ContactUsSuite
