package com.tokopedia.product.manage.item.main.base.data.repository;

import com.tokopedia.core.product.model.goldmerchant.Video;
import com.tokopedia.product.manage.item.main.base.data.mapper.EditProductFormMapper;
import com.tokopedia.product.manage.item.main.base.data.source.EditProductFormDataSource;
import com.tokopedia.product.manage.item.main.base.domain.EditProductFormRepository;
import com.tokopedia.product.manage.item.main.base.domain.model.UploadProductInputDomainModel;
import com.tokopedia.product.manage.item.video.data.source.FetchVideoEditProductDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/21/17.
 */

@Deprecated
public class EditProductFormRepositoryImpl implements EditProductFormRepository {
    private final EditProductFormDataSource editProductFormDataSource;
    private final EditProductFormMapper editProductFormMapper;
    private final FetchVideoEditProductDataSource fetchVideoEditProductDataSource;

    public EditProductFormRepositoryImpl(EditProductFormDataSource editProductFormDataSource, EditProductFormMapper editProductFormMapper, FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        this.editProductFormDataSource = editProductFormDataSource;
        this.editProductFormMapper = editProductFormMapper;
        this.fetchVideoEditProductDataSource = fetchVideoEditProductDataSource;
    }

    @Override
    public Observable<UploadProductInputDomainModel> fetchEditProduct(final String productId) {
        return Observable.zip(
                editProductFormDataSource.fetchEditProductForm(productId).map(editProductFormMapper),
                fetchVideoEditProductDataSource.fetchVideos(productId),
                new Func2<UploadProductInputDomainModel, List<Video>, UploadProductInputDomainModel>() {
                    @Override
                    public UploadProductInputDomainModel call(UploadProductInputDomainModel uploadProductInputDomainModel,
                                                              List<Video> videoList) {
                        List<String> videoIds = new ArrayList<>();
                        for (Video video : videoList) {
                            videoIds.add(video.getUrl());
                        }
                        uploadProductInputDomainModel.setProductVideos(videoIds);
                        return uploadProductInputDomainModel;
                    }
                }
        );
    }
}
