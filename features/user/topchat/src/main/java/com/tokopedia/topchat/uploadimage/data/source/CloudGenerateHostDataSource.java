//package com.tokopedia.topchat.uploadimage.data.source;
//
//import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
//import com.tokopedia.topchat.uploadimage.data.mapper.GenerateHostMapper;
//import com.tokopedia.topchat.uploadimage.domain.model.GenerateHostDomain;
//import com.tokopedia.usecase.RequestParams;
//
//import rx.Observable;
//
///**
// * @author by nisie on 9/5/17.
// */
//
//public class CloudGenerateHostDataSource {
//
//    private final GenerateHostActService generateHostActService;
//    private final GenerateHostMapper generateHostMapper;
//
//    public CloudGenerateHostDataSource(GenerateHostActService generateHostActService,
//                                       GenerateHostMapper generateHostMapper) {
//        this.generateHostActService = generateHostActService;
//        this.generateHostMapper = generateHostMapper;
//    }
//
//
//    public Observable<GenerateHostDomain> generateHost(RequestParams parameters) {
////        return generateHostActService.getApi()
////                .generateHost4(AuthUtil.generateParamsNetwork(MainApplication.getAppContext(),
////                        parameters.getParamsAllValueInString()))
////                .map(generateHostMapper);
//
//        //TODO AUTH UTIL
//        return generateHostActService.getApi()
//                .generateHost4(parameters.getParamsAllValueInString())
//                .map(generateHostMapper);
//    }
//}
