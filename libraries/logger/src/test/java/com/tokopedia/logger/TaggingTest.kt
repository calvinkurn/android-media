package com.tokopedia.logger

import com.tokopedia.logger.utils.Tag
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class TaggingTest {

    @Test
    fun `Set test`() {
        val tags: MutableSet<String> = mutableSetOf()
        val result: ArrayList<Long> = arrayListOf()
        for (i in 0 until dataSize) {
            val x = i+1
            tags.add("P$i#SPLASH_SCREEN_INIT$x#25#offline")
        }
        for(i in 0 until repeats) {
            val randomNumber = getRandomNumber()
            val t = measureTimeMillis {
                tags.find {
                    val tagSplit = it.split("#".toRegex()).dropLastWhile { tag -> tag.isEmpty() }.toTypedArray()
                    // Compare P and Tag
                    it.startsWith("P${randomNumber}") && tagSplit[1] == "SPLASH_SCREEN_INIT${randomNumber + 1}"
                }
            }
            result.add(t)
        }
        println("Average of $repeats set tests with data size of $dataSize took ${result.average()} ms")
    }

    @Test
    fun `Hash Map test`() {
        val tagsMap: HashMap<String, Tag> = hashMapOf()
        val result: ArrayList<Long> = arrayListOf()
        for (i in 0 until dataSize) {
            val x = i+1
            tagsMap["P$i#SPLASH_SCREEN_INIT$x"] = Tag(2)
        }
        for(i in 0 until repeats) {
            val randomNumber = getRandomNumber()
            val t = measureTimeMillis {
                tagsMap["P${randomNumber}#SPLASH_SCREEN_INIT${randomNumber + 1}"]
            }
            result.add(t)
        }
        println("Average of $repeats map tests with data size of $dataSize took ${result.average()} ms")
    }

    companion object{
        const val dataSize = 100
        const val repeats = 10
    }

    private fun getRandomNumber(): Int{
        return Random.nextInt(dataSize)
    }


}