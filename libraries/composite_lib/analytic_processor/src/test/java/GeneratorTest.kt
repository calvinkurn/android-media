import com.tokopedia.analytic.processor.AnnotationProcessor
import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourcesSubjectFactory
import org.junit.Test

class GeneratorTest {
    @Test
    fun validBundleThisWithNameAsKeyAndDefaultAllTrue() {
        val input = JavaFileObjects.forSourceString(
            "com.example.googletagmanagerwithannotation.Product",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;
                
                import com.example.annotation.BundleThis;

                @BundleThis
                public class Product {
                    String id;
                    String name;
                    String category;
                    String variant;
                    String brand;
                    String price;
                    String currency;
                }

            """.trimIndent()
        )

        val output = JavaFileObjects.forSourceString(
            "com.example.googletagmanagerwithannotation.ProductBundler",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;

                import android.os.Bundle;
                import android.util.Log;
                import com.analytic.util.BundlerUtil;
                import java.io.Serializable;
                import java.lang.ClassCastException;
                import java.lang.Object;
                import java.lang.String;
                import java.util.Map;
                
                public class ProductBundler {
                    public static Bundle getBundle(Product product) {
                        Bundle bundle = new Bundle();
                        BundlerUtil.putString("item_id", product.getId(),  "", bundle);
                        BundlerUtil.putString("item_name", product.getName(),  "", bundle);
                        BundlerUtil.putString("item_category", product.getCategory(),  "", bundle);
                        BundlerUtil.putString("item_variant", product.getVariant(),  "", bundle);
                        BundlerUtil.putString("item_brand", product.getBrand(),  "", bundle);
                        BundlerUtil.putDouble("price", product.getPrice(),  0.0, bundle);
                        BundlerUtil.putString("currency", product.getCurrency(),  "", bundle);
                        return bundle;
                    }
                
                    public static Bundle getBundle(Map<String, Object> data) {
                        Bundle bundle = new Bundle();
                        try  {
                            if (data.containsKey("item_id")) {
                                BundlerUtil.putString("item_id", (String) data.get("item_id"),  "", bundle);
                            }
                            if (data.containsKey("item_name")) {
                                BundlerUtil.putString("item_name", (String) data.get("item_name"),  "", bundle);
                            }
                            if (data.containsKey("item_category")) {
                                BundlerUtil.putString("item_category", (String) data.get("item_category"),  "", bundle);
                            }
                            if (data.containsKey("item_variant")) {
                                BundlerUtil.putString("item_variant", (String) data.get("item_variant"),  "", bundle);
                            }
                            if (data.containsKey("item_brand")) {
                                BundlerUtil.putString("item_brand", (String) data.get("item_brand"),  "", bundle);
                            }
                            if (data.containsKey("price")) {
                                BundlerUtil.putDouble("price", (double) data.get("price"),  0.0, bundle);
                            }
                            if (data.containsKey("currency")) {
                                BundlerUtil.putString("currency", (String) data.get("currency"),  "", bundle);
                            }
                        } catch (ClassCastException e)  {
                            Log.e("MapBundler", e.getMessage());
                        }
                        return bundle;
                    }
                }
            """.trimIndent()
        )

        Truth.assert_()
            .about(
                JavaSourcesSubjectFactory.javaSources()
            )
            .that(listOf(input))
            .processedWith(AnnotationProcessor())
            .compilesWithoutError()
    }
}