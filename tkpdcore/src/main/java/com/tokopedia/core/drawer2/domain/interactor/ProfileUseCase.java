package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.domain.ProfileRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileUseCase extends UseCase<ProfileModel> {

    private final ProfileRepository profileRepository;

    public ProfileUseCase(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          ProfileRepository profileRepository) {
        super(threadExecutor, postExecutionThread);
        this.profileRepository = profileRepository;
    }

    @Override
    public Observable<ProfileModel> createObservable(final RequestParams requestParams) {
        return profileRepository.getProfileFromLocal()
                .onErrorResumeNext(new Func1<Throwable, Observable<ProfileModel>>() {
                    @Override
                    public Observable<ProfileModel> call(Throwable throwable) {
                        return profileRepository.getProfileFromNetwork(requestParams.getParameters());
                    }
                });
    }
}