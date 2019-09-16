package com.tokopedia.imagesearch.domain.usecase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.imagesearch.R;
import com.tokopedia.imagesearch.data.mapper.ImageProductMapper;
import com.tokopedia.imagesearch.domain.model.SearchResultModel;
import com.tokopedia.imagesearch.helper.ImageHelper;
import com.tokopedia.imagesearch.network.response.ImageSearchProductResponse;
import com.tokopedia.imagesearch.search.exception.ImageNotSupportedException;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase extends UseCase<SearchResultModel> {


    private static final String IMAGE_CONTENT = "image";
    private static final String PARAMS = "params";
    private ImageProductMapper productMapper;
    private Context context;
    private GraphqlUseCase graphqlUseCase;

    private final int OPTIMUM_WIDTH = 300;
    private final int OPTIMUM_HEIGHT = 300;

    private final int MAX_WIDTH = 1280;
    private final int MAX_HEIGHT = 720;

    private final static String pageSize = "100";
    private final static String pageOffset = "0";

    private String imagePath;
    private final int MIN_WIDTH = 200;
    private final int MIN_HEIGHT = 200;

    public GetImageSearchUseCase(Context context,
                                 GraphqlUseCase graphqlUseCase,
                                 ImageProductMapper imageProductMapper) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.productMapper = imageProductMapper;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams params) {
        return Observable.just(imagePath)
                .flatMap(imagePath -> {
                    File imgFile = new File(imagePath);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    try {
                        if (myBitmap == null) {
                            myBitmap = ImageHelper.getBitmapFromUri(context, Uri.parse(imagePath),
                                    OPTIMUM_WIDTH, OPTIMUM_HEIGHT);
                        }

                        if (myBitmap.getWidth() < MIN_WIDTH ||
                                myBitmap.getHeight() < MIN_HEIGHT) {

                            throw new ImageNotSupportedException();

                        } else if (myBitmap.getWidth() > OPTIMUM_WIDTH ||
                                myBitmap.getHeight() > OPTIMUM_HEIGHT) {
                            myBitmap = ImageHelper.resizeImage(myBitmap, OPTIMUM_WIDTH, OPTIMUM_HEIGHT);
                        }

                        if (myBitmap.getHeight() > MAX_HEIGHT ||
                                myBitmap.getWidth() > MAX_WIDTH) {
                            throw new ImageNotSupportedException();
                        }

                        myBitmap = ImageHelper.RotatedBitmap(myBitmap, imagePath);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    String encodePicContent = Base64.encodeToString(byteArray,
                            Base64.NO_WRAP | Base64.NO_CLOSE);

                    Map<String, Object> mapContentVariable = new HashMap<>();
                    params.putString(IMAGE_CONTENT, encodePicContent);
                    params.putString(PARAMS, initializeSearchRequestParamForGql());

                    GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                            R.raw.query_image_search), ImageSearchProductResponse.class, params.getParameters());

                    graphqlUseCase.clearRequest();
                    graphqlUseCase.addRequest(graphqlRequest);
                    return graphqlUseCase.createObservable(RequestParams.EMPTY)
                            .map(productMapper);
                });
    }

    private static String initializeSearchRequestParamForGql() {
        return "page=" + String.valueOf(pageOffset) + "&page_size=" + pageSize + "&device=" + SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
