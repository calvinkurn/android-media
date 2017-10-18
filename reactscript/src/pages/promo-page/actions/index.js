import axios from 'axios'
import { NetworkModule, NavigationModule } from 'NativeModules'



const BASE_API_URL = {
    mojito_prod: 'https://mojito.tokopedia.com/os/api/v1/ospromo',
    mojito_staging: 'https://mojito-staging.tokopedia.com/os/api/v1/ospromo',
}



// =================== Reload State =================== //
export const RELOADSTATE = 'RELOADSTATE'
export const reloadState = () => {
    return {
        type: RELOADSTATE
    }
}


getEnv = () => {
    let app_env;
    return NavigationModule.getFlavor()
        .then(res => {
            if (res === 'live'){
                app_env = BASE_API_URL.mojito_prod
            } else if (res === 'staging'){
                app_env = BASE_API_URL.mojito_staging
            }
            return app_env
        })
        .catch(err => {
            console.log(err)
        })
}



// =================== Fetch Banner =================== //
export const FETCH_TOPBANNER = 'FETCH_TOPBANNER'
export const fetchTopBanner = (dataSlug) => {
    return {
        type: FETCH_TOPBANNER,
        payload: doTopBanner(dataSlug) 
    }
}

doTopBanner = async (dataSlug) => {
    const env = await getEnv(dataSlug)
    const banners = await fetchBanners(dataSlug, env)
    const titleToolbar = await setTitle(banners)
    return banners
}

setTitle = (banners) => {
    console.log(banners)
    console.log(banners.data.data.promo_name)
    const promo_name = banners.data.data.promo_name || 'Tokopedia'
    console.log(promo_name)
    return NavigationModule.setTitleToolbar(promo_name)
        .then(res => {
            console.log(res)
            return res
        })
        .catch(err => {
            console.log(err)
            return err
        })
}

fetchBanners = async (dataSlug, env) => {
    const url = `${env}/topcontent/${dataSlug.slug}?device=mobile`
    return NetworkModule.getResponse(url, "GET", '', true)
        .then(response => {
            let jsonResponse = JSON.parse(response)
            return jsonResponse
        })
        .catch(error => {
            return { data: [] }
        })
}




// =================== Fetch Products Categories =================== //
export const FETCH_CATEGORIES = 'FETCH_CATEGORIES'
export const fetchCategories = (dataSlug, offset, limit) => {
    return {
        type: FETCH_CATEGORIES,
        payload: doCategories(dataSlug, offset, limit)    
    }
}


doCategories = async (dataSlug, offset, limit) => {
    const env = await getEnv(dataSlug)
    const categories  = await fetcProdCategories(dataSlug, offset, limit, env)
    return categories
}


fetcProdCategories = async (dataSlug, offset, limit, env) => {
    const url = `${env}/categories?promo=${dataSlug.slug}&device=mobile&limit=${limit}&offset=${offset}`
    return NetworkModule.getResponse(url, 'GET', '', true)
        .then(res => {
            let jsonResponse = JSON.parse(res)
            return jsonResponse
        })
        .catch(err => {
            return { data: [] }
        })
}