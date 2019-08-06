package com.tokopedia.kol.feature.comment.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.feedcomponent.domain.usecase.GetMentionableUserUseCase;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
import com.tokopedia.kol.feature.comment.domain.interactor.DeleteKolCommentUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase;
import com.tokopedia.kol.feature.comment.domain.interactor.SendKolCommentUseCase;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.subscriber.DeleteKolCommentSubscriber;
import com.tokopedia.kol.feature.comment.view.subscriber.GetKolCommentFirstTimeSubscriber;
import com.tokopedia.kol.feature.comment.view.subscriber.GetKolCommentSubscriber;
import com.tokopedia.kol.feature.comment.view.subscriber.SendKolCommentSubscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentPresenter extends BaseDaggerPresenter<KolComment.View>
        implements KolComment.Presenter {

    private final GetKolCommentsUseCase getKolCommentsUseCase;
    private final SendKolCommentUseCase sendKolCommentUseCase;
    private final DeleteKolCommentUseCase deleteKolCommentUseCase;
    private final GetMentionableUserUseCase getMentionableUserUseCase;

    private String cursor;

    @Override
    public void detachView() {
        super.detachView();
        getKolCommentsUseCase.unsubscribe();
        sendKolCommentUseCase.unsubscribe();
        deleteKolCommentUseCase.unsubscribe();
        getMentionableUserUseCase.unsubscribe();
    }

    @Inject
    public KolCommentPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                               SendKolCommentUseCase sendKolCommentUseCase,
                               DeleteKolCommentUseCase deleteKolCommentUseCase,
                               GetMentionableUserUseCase getMentionableUserUseCase) {
        this.getKolCommentsUseCase = getKolCommentsUseCase;
        this.sendKolCommentUseCase = sendKolCommentUseCase;
        this.deleteKolCommentUseCase = deleteKolCommentUseCase;
        this.getMentionableUserUseCase = getMentionableUserUseCase;
    }

    @Override
    public void getCommentFirstTime(int id) {
        getView().showLoading();
        getKolCommentsUseCase.execute(
                GetKolCommentsUseCase.getFirstTimeParam(id),
                new GetKolCommentFirstTimeSubscriber(getView()));
    }

    @Override
    public void loadMoreComments(int id) {
        getKolCommentsUseCase.execute(
                GetKolCommentsUseCase.getParam(id, cursor),
                new GetKolCommentSubscriber(getView()));
    }

    @Override
    public void updateCursor(String lastcursor) {
        this.cursor = lastcursor;
    }

    @Override
    public void deleteComment(String id, int adapterPosition) {
        getView().showProgressDialog();
        deleteKolCommentUseCase.execute(
                DeleteKolCommentUseCase.getParam(Integer.parseInt(id)),
                new DeleteKolCommentSubscriber(getView(), adapterPosition));
    }

    @Override
    public void sendComment(int id, String comment) {
        if (isValid(comment)) {
            getView().showProgressDialog();
            getView().disableSendComment();
            sendKolCommentUseCase.execute(
                    SendKolCommentUseCase.getParam(id, comment),
                    new SendKolCommentSubscriber(getView()));
        }
    }

    @Override
    public void getMentionableUserByKeyword(String keyword) {
        if (keyword != null && !TextUtils.isEmpty(keyword)) {
            getMentionableUserUseCase.unsubscribe();
            getMentionableUserUseCase.execute(
                    GetMentionableUserUseCase.Companion.getParam(keyword),
                    new Subscriber<List<? extends MentionableUserViewModel>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<? extends MentionableUserViewModel> mentionableUserViewModels) {
                            getView().showMentionUserSuggestionList((List<MentionableUserViewModel>) mentionableUserViewModels);
                        }
                    }
            );
        }
    }

    private boolean isValid(String comment) {
        return comment.trim().length() > 0;
    }
}
