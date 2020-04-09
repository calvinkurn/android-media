import com.example.processor.AnnotationProcessor
import com.google.common.truth.Truth
import com.google.testing.compile.JavaFileObjects.forSourceString
import com.google.testing.compile.JavaSourcesSubjectFactory
import org.junit.Test

class GeneratorTest {
    @Test
    fun validBundleThisWithNameAsKeyAndDefaultAllTrue() {
        val input = forSourceString(
            "com.example.googletagmanagerwithannotation.Product",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;
                
                import com.example.annotation.BundleThis;

                @BundleThis
                public class Product {
                    String id;
                }

            """.trimIndent()
        )

        val output = forSourceString(
            "com.example.googletagmanagerwithannotation.ProductBundler",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;

                import android.os.Bundle;
                import android.util.Log;
                import com.analytic.util.BundlerUtil;
                import java.lang.ClassCastException;
                import java.lang.Object;
                import java.lang.String;
                import java.util.Map;
                
                public class ProductBundler {
                    public static Bundle getBundle(Product product) {
                        Bundle bundle = new Bundle();
                        BundlerUtil.putString("id", product.id,  "", bundle);
                        return bundle;
                    }
                
                    public static Bundle getBundle(Map<String, Object> data) {
                        Bundle bundle = new Bundle();
                        try  {
                            if (data.containsKey("id")) {
                                BundlerUtil.putString("id", (String) data.get("id"),  "", bundle);
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
            .and()
            .generatesSources(output)
    }

    @Test
    fun bundleThisWithNameAsKeyAndDefaultAllTrueWithCustomDefaultValue() {
        val input = forSourceString(
            "com.example.googletagmanagerwithannotation.Product",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;
                
                import com.example.annotation.BundleThis;
                import com.example.annotation.defaultvalues.DefaultValueString;

                @BundleThis
                public class Product {
                    @DefaultValueString(value = "Some String")
                    String id;
                }

            """.trimIndent()
        )

        val output = forSourceString(
            "com.example.googletagmanagerwithannotation.ProductBundler",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;

                import android.os.Bundle;
                import android.util.Log;
                import com.analytic.util.BundlerUtil;
                import java.lang.ClassCastException;
                import java.lang.Object;
                import java.lang.String;
                import java.util.Map;
                
                public class ProductBundler {
                    public static Bundle getBundle(Product product) {
                        Bundle bundle = new Bundle();
                        BundlerUtil.putString("id", product.id,  "Some String", bundle);
                        return bundle;
                    }
                
                    public static Bundle getBundle(Map<String, Object> data) {
                        Bundle bundle = new Bundle();
                        try  {
                            if (data.containsKey("id")) {
                                BundlerUtil.putString("id", (String) data.get("id"),  "Some String", bundle);
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
            .and()
            .generatesSources(output)
    }

    @Test
    fun bundleThisWithNameAsKeyFalseAndDefaultAllTrue() {
        val input = forSourceString(
            "com.example.googletagmanagerwithannotation.Product",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;
                
                import com.example.annotation.BundleThis;

                @BundleThis(nameAsKey = false, defaultAll = true)
                public class Product {
                    String id;
                }

            """.trimIndent()
        )

        val output = forSourceString(
            "com.example.googletagmanagerwithannotation.ProductBundler",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;

                import android.os.Bundle;
                import android.util.Log;
                import java.lang.ClassCastException;
                import java.lang.Object;
                import java.lang.String;
                import java.util.Map;
                
                public class ProductBundler {
                    public static Bundle getBundle(Product product) {
                        Bundle bundle = new Bundle();
                        return bundle;
                    }
                
                    public static Bundle getBundle(Map<String, Object> data) {
                        Bundle bundle = new Bundle();
                        try  {
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
            .and()
            .generatesSources(output)
    }

    @Test
    fun invalidBundleThisWithNameAsKeyAndDefaultAllTrue() {
        val input = forSourceString(
            "com.example.googletagmanagerwithannotation.Product",
            """
                package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce;
                
                import com.example.annotation.BundleThis;
                import com.example.annotation.Key;

                @BundleThis
                public class Product {
                    @Key(key = "item_id")
                    String id;
                }

            """.trimIndent()
        )

        Truth.assert_()
            .about(
                JavaSourcesSubjectFactory.javaSources()
            )
            .that(listOf(input))
            .processedWith(AnnotationProcessor())
            .failsToCompile()
            .withErrorContaining("Property id has duplicate key definition")
    }
}