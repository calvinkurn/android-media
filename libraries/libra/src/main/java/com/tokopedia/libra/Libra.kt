package com.tokopedia.libra

interface Libra {

    /**
     * A libra fetcher.
     *
     * This method response to fetch the list of availability
     * of Libra's experiment based on [LibraOwner].
     *
     * This method needs to call in between onCreate or onStart lifecycle,
     * hence every single those lifecycle got triggered, a new values
     * will be got updated in the cache.
     *
     * This method is fire-and-forget cycle, thus to get a Libra values,
     * we have to wait for the (N + 1)-session.
     *
     * @param: [LibraOwner] a owner of experiment
     */
    suspend fun fetch(owner: LibraOwner)

    /**
     * Get variant detail based on [LibraOwner] and experiment name.
     *
     * @param: [LibraOwner] a owner of experiment
     * @param: [experiment] the name of experiment
     * @return: A [LibraResult] of experimental data, invalid if return an empty string.
     */
    fun variant(owner: LibraOwner, experiment: String): LibraResult

    /**
     * Get a variant result based on [LibraOwner] and experiment name.
     *
     * @param: [LibraOwner] a owner of experiment
     * @param: [experiment] the name of experiment
     * @return: A string of variant status, invalid if return an empty string.
     */
    fun variantAsString(owner: LibraOwner, experiment: String): String

    /**
     * A variant getter with state version.
     *
     * This return type uses [LibraState] to make ot type safety of
     * differentiate between control and variant. Hence, the page owner
     * doesn't need to managing an inconsistency of experiment handler.
     */
    fun variantAsState(owner: LibraOwner, experiment: String): LibraState

    /**
     * Due to this Libra's library uses a SharedPref to store the latest value,
     * thus to clear the active cache, we have to invoke this method.
     */
    fun clear(owner: LibraOwner)
}
