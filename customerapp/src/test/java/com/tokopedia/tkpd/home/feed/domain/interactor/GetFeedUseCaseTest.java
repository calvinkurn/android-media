package com.tokopedia.tkpd.home.feed.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Kulomady on 2/2/17.
 */
public class GetFeedUseCaseTest {


    @Mock
    private ThreadExecutor threadExecutor;
    @Mock
    private PostExecutionThread postExecutionThread;
    @Mock
    private FeedRepository feedRepository;
    @Mock
    private RequestParams mockRequestParam;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private GetFeedUseCase getFeedUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getFeedUseCase = new GetFeedUseCase(
                threadExecutor, postExecutionThread, feedRepository);
    }

    @Test
    public void testCreateobservable() throws Exception {
        getFeedUseCase.createObservable(mockRequestParam);
        verify(feedRepository).getFeed(true, mockRequestParam.getParameters());
        verifyNoMoreInteractions(feedRepository);
        verifyZeroInteractions(threadExecutor);
        verifyZeroInteractions(postExecutionThread);
    }

    @Test
    public void testMustThrowNullpointerExeptions() throws Exception {
        expectedException.expect(NullPointerException.class);
        getFeedUseCase.createObservable(null);
    }
}