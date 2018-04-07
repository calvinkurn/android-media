package com.tokopedia.posapp.product.common.data.source.cloud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.mapper.GetProductListMapper;
import com.tokopedia.posapp.product.productdetail.data.mapper.GetProductMapper;
import com.tokopedia.posapp.product.productdetail.data.source.cloud.api.ProductApi;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/9/17.
 */

public class ProductCloudSource {
    public static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SHOP_ID = "shop_id";

    private ProductApi productApi;
    private ProductListApi productListApi;
    private GetProductMapper getProductMapper;
    private GetProductListMapper getProductListMapper;
    private Gson gson;

    @Inject
    public ProductCloudSource(Gson gson,
                              ProductApi productApi,
                              ProductListApi productListApi,
                              GetProductMapper getProductMapper,
                              GetProductListMapper getProductListMapper) {
        this.gson = gson;
        this.productApi = productApi;
        this.productListApi = productListApi;
        this.getProductMapper = getProductMapper;
        this.getProductListMapper = getProductListMapper;
    }

    public Observable<ProductDetailData> getProduct(RequestParams params) {
//        return Observable.just(mockPdp)
//                .map(new Func1<String, ProductDetailData>() {
//                    @Override
//                    public ProductDetailData call(String s) {
//                        DataResponse<ProductDetailData> response = gson.fromJson(mockPdp, new TypeToken<DataResponse<ProductDetailData>>(){}.getType());
//                        return response.getData();
//                    }
//                });
        return productApi.getProductDetail(params.getParamsAllValueInString()).map(getProductMapper);
    }

    public Observable<List<ProductDomain>> getProductList(RequestParams params) {
        return productListApi
                .getProductListV2(params.getParamsAllValueInString())
                .map(getProductListMapper);
    }
    private static final String mockPdp = "{\n"+
            "    \"status\": \"OK\",\n"+
            "    \"data\": {\n"+
            "        \"info\": {\n"+
            "            \"product_name\": \"STABILO Pen Point 88 Fineliner (Satuan)\",\n"+
            "            \"product_id\": 15203620,\n"+
            "            \"product_key\": \"stabilo-pen-point-88-fineliner-satuan\",\n"+
            "            \"product_price\": \"Rp 8.650\",\n"+
            "            \"product_price_unfmt\": 8650,\n"+
            "            \"product_last_update\": \"23-01-2018, 17:31 WIB\",\n"+
            "            \"product_description\": \"- Salah satu fineliner pen terbaik di Eropa<br/>- Tip berukuran 0.4mm - Water-based ink<br/>- Terdiri dari warna-warna cerah, tidak mudah kering walau tutupnya lama terbuka, serta tidak belobor<br/>- Ujung tip dilapisi logam sehingga lebih awet dan tahan lama serta bisa digunakan dengan penggaris atau stencils.<br/>- Cocok dipakai untuk menulis, menggambar, dll<br/>- Terdiri dari 30 Warna<br/><br/>Ice Green<br/>Night Blue<br/>Apricot<br/>Blue Middle<br/>Apple Green<br/>Green<br/>Red<br/>Blue <br/>Light Green<br/>Yellow<br/>Brown<br/>Black<br/>Red Deep<br/>Turquoise Blue<br/>Pine Green<br/>Orange<br/>Violet<br/>Pink<br/>Light Blue<br/>Lilac<br/>Light Lilac<br/>Earth Green<br/>Dark Ochre<br/>Light Grey<br/>Dark Grey<br/>Neon Yellow<br/>Neon Green<br/>Neon Red<br/>Neon Orange<br/>Neon Pink<br/><br/>Masukkan Keterangan warna yang anda inginkan disaat memesan<br/>*Ketersedian warna harap ditanyakan terlebih dahulu<br/><br/>\",\n"+
            "            \"product_min_order_fmt\": \"1\",\n"+
            "            \"product_min_order\": 1,\n"+
            "            \"product_max_order\": 0,\n"+
            "            \"product_max_order_fmt\": \"0\",\n"+
            "            \"product_status\": \"1\",\n"+
            "            \"product_weight\": \"10\",\n"+
            "            \"product_weight_unit\": \"gr\",\n"+
            "            \"product_condition\": \"Baru\",\n"+
            "            \"product_insurance\": \"Opsional\",\n"+
            "            \"product_url\": \"https://staging.tokopedia.com/o2o/stabilo-pen-point-88-fineliner-satuan\",\n"+
            "            \"catalog_id\": 0,\n"+
            "            \"catalog_name\": 0,\n"+
            "            \"catalog_url\": 0,\n"+
            "            \"product_status_title\": \"\",\n"+
            "            \"product_status_message\": \"\",\n"+
            "            \"product_price_alert\": 0,\n"+
            "            \"product_etalase_id\": \"1403740\",\n"+
            "            \"product_etalase\": \"Writing\",\n"+
            "            \"product_etalase_url\": \"https://staging.tokopedia.com/o2o/etalase/1403740\",\n"+
            "            \"product_returnable\": \"-1\",\n"+
            "            \"return_info\": {\n"+
            "                \"icon\": \"\",\n"+
            "                \"color_hex\": \"\",\n"+
            "                \"color_rgb\": \"\",\n"+
            "                \"content\": \"\"\n"+
            "            },\n"+
            "            \"product_already_wishlist\": 0,\n"+
            "            \"product_installments\": [\n"+
            "                {\n"+
            "                    \"id\": 8,\n"+
            "                    \"name\": \"ANZ\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-anz.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 820\",\n"+
            "                            \"percentage\": \"1.79\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.552\",\n"+
            "                            \"percentage\": \"1.99\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 1,\n"+
            "                    \"name\": \"BCA\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-bca.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.75\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.552\",\n"+
            "                            \"percentage\": \"1.25\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 99,\n"+
            "                    \"name\": \"BNI\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-bni.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.8\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.8\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 3,\n"+
            "                    \"name\": \"BRI\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-bri.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 721\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"18\": {\n"+
            "                            \"installment_price\": \"Rp 664\",\n"+
            "                            \"percentage\": \"2.5\",\n"+
            "                            \"min_purchase\": \"Rp 3.000.000\"\n"+
            "                        },\n"+
            "                        \"24\": {\n"+
            "                            \"installment_price\": \"Rp 630\",\n"+
            "                            \"percentage\": \"3\",\n"+
            "                            \"min_purchase\": \"Rp 3.000.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.442\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 16,\n"+
            "                    \"name\": \"Bukopin\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-bukopin.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 721\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 1.500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.442\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 1.000.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 12,\n"+
            "                    \"name\": \"Citibank\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-citibank.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 721\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.442\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 17,\n"+
            "                    \"name\": \"Danamon\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-danamon.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 721\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.442\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 7,\n"+
            "                    \"name\": \"HSBC\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-hsbc.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 2,\n"+
            "                    \"name\": \"Mandiri\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-mandiri.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.8\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.8\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 10,\n"+
            "                    \"name\": \"MayBank\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-maybank.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.927\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 14,\n"+
            "                    \"name\": \"OCBC NISP\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-ocbc_nisp.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 18,\n"+
            "                    \"name\": \"Panin\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-panin.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 721\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.442\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 15,\n"+
            "                    \"name\": \"Permata\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-permata.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 721\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.442\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 6,\n"+
            "                    \"name\": \"Standard Chartered\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-standard_chartered.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"18\": {\n"+
            "                            \"installment_price\": \"Rp 664\",\n"+
            "                            \"percentage\": \"2.5\",\n"+
            "                            \"min_purchase\": \"Rp 3.000.000\"\n"+
            "                        },\n"+
            "                        \"24\": {\n"+
            "                            \"installment_price\": \"Rp 630\",\n"+
            "                            \"percentage\": \"3\",\n"+
            "                            \"min_purchase\": \"Rp 3.000.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                },\n"+
            "                {\n"+
            "                    \"id\": 11,\n"+
            "                    \"name\": \"UOB\",\n"+
            "                    \"icon\": \"https://ecs7.tokopedia.net/img/icon-uob.png\",\n"+
            "                    \"terms\": {\n"+
            "                        \"12\": {\n"+
            "                            \"installment_price\": \"Rp 732\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"18\": {\n"+
            "                            \"installment_price\": \"Rp 664\",\n"+
            "                            \"percentage\": \"2.5\",\n"+
            "                            \"min_purchase\": \"Rp 3.000.000\"\n"+
            "                        },\n"+
            "                        \"24\": {\n"+
            "                            \"installment_price\": \"Rp 630\",\n"+
            "                            \"percentage\": \"3\",\n"+
            "                            \"min_purchase\": \"Rp 3.000.000\"\n"+
            "                        },\n"+
            "                        \"3\": {\n"+
            "                            \"installment_price\": \"Rp 2.884\",\n"+
            "                            \"percentage\": \"0\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        },\n"+
            "                        \"6\": {\n"+
            "                            \"installment_price\": \"Rp 1.464\",\n"+
            "                            \"percentage\": \"0.5\",\n"+
            "                            \"min_purchase\": \"Rp 500.000\"\n"+
            "                        }\n"+
            "                    }\n"+
            "                }\n"+
            "            ],\n"+
            "            \"installment_min_percentage\": \"3%\",\n"+
            "            \"installment_min_price\": \"Rp 630\",\n"+
            "            \"wholesale_min_price\": \"Rp 7.000\",\n"+
            "            \"wholesale_min_quantity\": \"11\",\n"+
            "            \"has_variant\": false\n"+
            "        },\n"+
            "        \"shop_info\": {\n"+
            "            \"shop_owner_id\": 5515762,\n"+
            "            \"shop_id\": \"480394\",\n"+
            "            \"shop_name\": \"Offline to Online\",\n"+
            "            \"shop_url\": \"https://staging.tokopedia.com/o2o\",\n"+
            "            \"shop_domain\": \"o2o\",\n"+
            "            \"shop_tagline\": \"Branding with Tokopedia POS in malls\",\n"+
            "            \"shop_description\": \"Branding with Tokopedia POS in malls\",\n"+
            "            \"shop_open_since\": \"\",\n"+
            "            \"shop_reputation_badge\": 0,\n"+
            "            \"shop_min_badge_score\": 0,\n"+
            "            \"shop_status\": 1,\n"+
            "            \"shop_location\": \"DKI Jakarta\",\n"+
            "            \"shop_district_id\": 2270,\n"+
            "            \"shop_district_name\": \"Setiabudi\",\n"+
            "            \"shop_city_id\": 175,\n"+
            "            \"shop_province_id\": 13,\n"+
            "            \"shop_geolocation\": \"\",\n"+
            "            \"shop_avatar\": \"https://imagerouter-staging.tokopedia.com/img/215/shops-1/2018/1/23/5515762/5515762_f2a5ac51-d47d-429d-9f2a-57208bfb3f91.jpg?user_id=5515762\\u0026product_id=15203620\",\n"+
            "            \"shop_owner_last_login\": \"\",\n"+
            "            \"shop_already_favorited\": 0,\n"+
            "            \"shop_total_favorit\": 0,\n"+
            "            \"shop_cover\": \"https://ecs7.tokopedia.net/img/default_v3-shopnocover.png\",\n"+
            "            \"shop_is_gold\": 0,\n"+
            "            \"shop_is_gold_badge\": false,\n"+
            "            \"shop_lucky\": \"https://clover-staging.tokopedia.com/badges/merchant/v1?shop_id=480394\",\n"+
            "            \"shop_is_owner\": 1,\n"+
            "            \"shop_is_allow_manage\": 1,\n"+
            "            \"shop_has_terms\": 0,\n"+
            "            \"shop_is_closed_note\": \"\",\n"+
            "            \"shop_is_closed_reason\": \"\",\n"+
            "            \"shop_is_closed_until\": \"\",\n"+
            "            \"shop_reputation\": \"0\",\n"+
            "            \"shop_status_title\": \"\",\n"+
            "            \"shop_status_message\": \"\",\n"+
            "            \"shop_gold_expired_time\": 0,\n"+
            "            \"shop_is_free_returns\": \"0\",\n"+
            "            \"shop_is_official\": \"0\",\n"+
            "            \"shop_official_top\": \"https://ws-staging.tokopedia.com/ssi/shop-static/o2o/_apps/top\",\n"+
            "            \"shop_official_bot\": \"https://ws-staging.tokopedia.com/ssi/shop-static/o2o/_apps/bot\",\n"+
            "            \"badges\": [\n"+
            "                {\n"+
            "                    \"title\": \"Lucky Merchant\",\n"+
            "                    \"image_url\": \"https://clover-staging.tokopedia.com/badges/merchant/v1?shop_id=480394\"\n"+
            "                }\n"+
            "            ],\n"+
            "            \"shop_stats\": {\n"+
            "                \"shop_badge_level\": {\n"+
            "                    \"level\": 0,\n"+
            "                    \"set\": 0\n"+
            "                },\n"+
            "                \"shop_reputation_score\": \"0\"\n"+
            "            },\n"+
            "            \"shop_shipments\": [\n"+
            "                {\n"+
            "                    \"shipping_id\": \"1\",\n"+
            "                    \"shipping_name\": \"JNE\",\n"+
            "                    \"shipping_code\": \"jne\",\n"+
            "                    \"logo\": \"https://ecs7.tokopedia.net/img/kurir-jne.png\",\n"+
            "                    \"package_names\": [\n"+
            "                        \"YES\",\n"+
            "                        \"Reguler\",\n"+
            "                        \"OKE\"\n"+
            "                    ]\n"+
            "                },\n"+
            "                {\n"+
            "                    \"shipping_id\": \"10\",\n"+
            "                    \"shipping_name\": \"Go-Send\",\n"+
            "                    \"shipping_code\": \"gojek\",\n"+
            "                    \"logo\": \"https://ecs7.tokopedia.net/img/kurir-gosend.png\",\n"+
            "                    \"package_names\": [\n"+
            "                        \"Instant Courier\",\n"+
            "                        \"Same Day\"\n"+
            "                    ]\n"+
            "                },\n"+
            "                {\n"+
            "                    \"shipping_id\": \"12\",\n"+
            "                    \"shipping_name\": \"Ninja Xpress\",\n"+
            "                    \"shipping_code\": \"ninja\",\n"+
            "                    \"logo\": \"https://ecs7.tokopedia.net/img/kurir-ninja.png\",\n"+
            "                    \"package_names\": [\n"+
            "                        \"Reguler\"\n"+
            "                    ]\n"+
            "                },\n"+
            "                {\n"+
            "                    \"shipping_id\": \"13\",\n"+
            "                    \"shipping_name\": \"Grab\",\n"+
            "                    \"shipping_code\": \"grab\",\n"+
            "                    \"logo\": \"https://ecs7.tokopedia.net/img/kurir-grab.png\",\n"+
            "                    \"package_names\": [\n"+
            "                        \"Instant\",\n"+
            "                        \"Same Day\"\n"+
            "                    ]\n"+
            "                }\n"+
            "            ]\n"+
            "        },\n"+
            "        \"rating\": {\n"+
            "            \"product_accuracy_star_rate\": 5,\n"+
            "            \"product_accuracy_star_desc\": \"Sangat Baik\",\n"+
            "            \"product_netral_review_rate_accuracy\": \"0\",\n"+
            "            \"product_netral_review_rating\": \"0\",\n"+
            "            \"product_positive_review_rate_accuracy\": \"100\",\n"+
            "            \"product_positive_review_rating\": \"100\",\n"+
            "            \"product_negative_review_rate_accuracy\": \"0\",\n"+
            "            \"product_negative_review_rating\": \"0\",\n"+
            "            \"product_rate_accuracy\": 100,\n"+
            "            \"product_rate_accuracy_point\": \"5.0\",\n"+
            "            \"product_rating\": 0,\n"+
            "            \"product_rating_point\": \"0.0\",\n"+
            "            \"product_rating_star_point\": 0,\n"+
            "            \"product_rating_star_desc\": \"Sangat Baik\",\n"+
            "            \"product_review\": 0,\n"+
            "            \"product_rating_list\": []\n"+
            "        },\n"+
            "        \"preorder\": {\n"+
            "            \"preorder_status\": 0,\n"+
            "            \"preorder_process_time_type\": 0,\n"+
            "            \"preorder_process_time_type_string\": 0,\n"+
            "            \"preorder_process_time\": 0\n"+
            "        },\n"+
            "        \"statistic\": {\n"+
            "            \"product_sold_count\": \"0\",\n"+
            "            \"product_transaction_count\": \"0\",\n"+
            "            \"product_rating_point\": \"0\",\n"+
            "            \"product_cancel_rate\": \"0\",\n"+
            "            \"product_review_count\": \"0\",\n"+
            "            \"product_view_count\": \"17\",\n"+
            "            \"product_success_rate\": \"0\",\n"+
            "            \"product_rating_desc\": \"Tidak ada\",\n"+
            "            \"product_talk_count\": \"0\"\n"+
            "        },\n"+
            "        \"wholesale_price\": [\n"+
            "            {\n"+
            "                \"wholesale_min\": \"2\",\n"+
            "                \"wholesale_price\": \"8.000\",\n"+
            "                \"wholesale_max\": \"3\"\n"+
            "            },\n"+
            "            {\n"+
            "                \"wholesale_min\": \"4\",\n"+
            "                \"wholesale_price\": \"7.500\",\n"+
            "                \"wholesale_max\": \"10\"\n"+
            "            },\n"+
            "            {\n"+
            "                \"wholesale_min\": \"11\",\n"+
            "                \"wholesale_price\": \"7.000\",\n"+
            "                \"wholesale_max\": \"21\"\n"+
            "            }\n"+
            "        ],\n"+
            "        \"breadcrumb\": [\n"+
            "            {\n"+
            "                \"department_name\": \"Highlighter (Stabilo)\",\n"+
            "                \"department_identifier\": \"office-stationery_alat-tulis_highlighter-stabilo\",\n"+
            "                \"department_dir_view\": 0,\n"+
            "                \"department_id\": \"1079\",\n"+
            "                \"department_tree\": 3\n"+
            "            },\n"+
            "            {\n"+
            "                \"department_name\": \"Alat Tulis\",\n"+
            "                \"department_identifier\": \"office-stationery_alat-tulis\",\n"+
            "                \"department_dir_view\": 0,\n"+
            "                \"department_id\": \"1076\",\n"+
            "                \"department_tree\": 2\n"+
            "            },\n"+
            "            {\n"+
            "                \"department_name\": \"Office & Stationery\",\n"+
            "                \"department_identifier\": \"office-stationery\",\n"+
            "                \"department_dir_view\": 0,\n"+
            "                \"department_id\": \"642\",\n"+
            "                \"department_tree\": 1\n"+
            "            }\n"+
            "        ],\n"+
            "        \"product_images\": [\n"+
            "            {\n"+
            "                \"image_id\": 21100877,\n"+
            "                \"image_src_300\": \"https://ecs7.tokopedia.net/img/cache/300/product-1/2018/1/23/5515762/5515762_43a85a27-2a84-4be3-90eb-80368585aa6c_883_510.jpg\",\n"+
            "                \"image_status\": 2,\n"+
            "                \"image_description\": \"\",\n"+
            "                \"image_primary\": 1,\n"+
            "                \"image_src\": \"https://ecs7.tokopedia.net/img/cache/700/product-1/2018/1/23/5515762/5515762_43a85a27-2a84-4be3-90eb-80368585aa6c_883_510.jpg\"\n"+
            "            },\n"+
            "            {\n"+
            "                \"image_id\": 21100878,\n"+
            "                \"image_src_300\": \"https://ecs7.tokopedia.net/img/cache/300/product-1/2018/1/23/5515762/5515762_7f23df4e-efe9-446b-bf9f-77b8c10330de_800_800.jpg\",\n"+
            "                \"image_status\": 1,\n"+
            "                \"image_description\": \"\",\n"+
            "                \"image_primary\": 0,\n"+
            "                \"image_src\": \"https://ecs7.tokopedia.net/img/cache/700/product-1/2018/1/23/5515762/5515762_7f23df4e-efe9-446b-bf9f-77b8c10330de_800_800.jpg\"\n"+
            "            }\n"+
            "        ],\n"+
            "        \"cashback\": {\n"+
            "            \"product_cashback\": \"\",\n"+
            "            \"product_cashback_value\": \"\"\n"+
            "        },\n"+
            "        \"campaign\": {\n"+
            "            \"is_active\": false,\n"+
            "            \"original_price\": 0,\n"+
            "            \"original_price_fmt\": \"\",\n"+
            "            \"discounted_percentage\": 0,\n"+
            "            \"discounted_price\": 0,\n"+
            "            \"discounted_price_fmt\": \"\",\n"+
            "            \"campaign_type\": 0,\n"+
            "            \"start_date\": \"\",\n"+
            "            \"end_date\": \"\"\n"+
            "        }\n"+
            "    },\n"+
            "    \"server_process_time\": \"0.046799\"\n"+
            "}";
}
