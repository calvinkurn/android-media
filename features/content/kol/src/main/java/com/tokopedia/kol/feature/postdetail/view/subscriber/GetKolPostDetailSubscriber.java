package com.tokopedia.kol.feature.postdetail.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel;
import com.tokopedia.kol.common.util.TimeConverter;
import com.tokopedia.kol.feature.comment.data.pojo.get.Comment;
import com.tokopedia.kol.feature.comment.data.pojo.get.Content;
import com.tokopedia.kol.feature.comment.data.pojo.get.PostKol;
import com.tokopedia.kol.feature.comment.data.pojo.get.Tag;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.domain.interactor.GetKolPostDetailUseCase;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by milhamj on 27/07/18.
 */

public class GetKolPostDetailSubscriber extends Subscriber<PostDetailViewModel> {

    private final KolPostDetailContract.View view;

    public GetKolPostDetailSubscriber(KolPostDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        view.dismissLoading();
        view.onErrorGetKolPostDetail(ErrorHandler.getErrorMessage(view.getContext(), e));
        view.stopTrace();
    }

    @Override
    public void onNext(PostDetailViewModel postDetailViewModel) {

        view.dismissLoading();
        List<Visitable> list = new ArrayList<>();
        list.addAll(postDetailViewModel.getDynamicPostViewModel().getPostList());
        if(!list.isEmpty()) {
            view.onSuccessGetKolPostDetail(list, postDetailViewModel);
        }else{
            view.onEmptyDetailFeed();
        }
        view.stopTrace();

    }

}
