import React, { Component } from 'react'
import { View, ActivityIndicator, DeviceEventEmitter, AsyncStorage } from 'react-native'
import { connect } from 'react-redux'
import { fetchCampaigns, addWishlistFromPdp, removeWishlistFromPdp } from '../actions/actions'
import CampaignList from '../components/campaignList'



class CampaignContainer extends Component {
    componentDidMount() {
        const { dispatch } = this.props
        AsyncStorage.getItem('user_id')
        .then(uid => {
            dispatch(fetchCampaigns(uid))
        })
        // const User_ID = this.props.screenProps.User_ID
        // dispatch(fetchCampaigns(User_ID))
        
        this.addToWishlist = DeviceEventEmitter.addListener("WishlistAdd", (res) => {
            dispatch(addWishlistFromPdp(res))
        });
        this.removeWishlist = DeviceEventEmitter.addListener("WishlistRemove", (res) => {
            dispatch(removeWishlistFromPdp(res))
        });
        this.checkLogin = DeviceEventEmitter.addListener('Login', (res) => {
            const userid_from_login_os = res.user_id
            AsyncStorage.setItem('user_id', userid_from_login_os);
            
            AsyncStorage.getItem('user_id')
            .then(uid => {
                dispatch(fetchCampaigns(uid))
            })
        })
    }

    componentWillUnmount(){
        this.addToWishlist.remove()
        this.removeWishlist.remove()
        this.checkLogin.remove()
    }
    
    renderCampaign = (campaigns) => {
        let uuid;
        AsyncStorage.getItem('user_id').then(val => { uuid: val })
        const userid = uuid === '' ? this.props.screenProps.User_ID : uuid

        return (
            this.props.campaigns.isFetching ? 
            <View style={{ marginTop:20, marginBottom:20, justifyContent:'center', alignItems:'center', flex:1}}>
                <ActivityIndicator size="large" />
            </View> : 
            <CampaignList campaigns={campaigns} />
        )
    }
    
    render() {
        const campaigns = this.props.campaigns.items
        if (campaigns.length == 1){
            return this.renderCampaign(campaigns[0])
        } else {
            return this.renderCampaign(campaigns)
        }
    }
}

const mapStateToProps = state => {
    const campaigns = state.campaigns
    return {
        campaigns
    }
}

export default connect(mapStateToProps)(CampaignContainer)