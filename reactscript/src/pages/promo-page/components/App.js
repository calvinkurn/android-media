import React, { Component } from 'react'
import {
    View,
    ScrollView
} from 'react-native'
import { reloadState } from '../actions/index'
import { connect } from 'react-redux'
import BackToTop from './BackToTop'
import CategoryContainer from '../containers/CategoryContainer'


class App extends Component {
    componentDidMount(){
        console.log(this.props)
    }


    render() {
        return (
            <CategoryContainer
                dataSlug={this.props.screenProps}
                navigation={this.props.navigation}
                termsConditions={this.props.dataTermsConditions}
            />
        )
    }

    static navigationOptions = {
        header: null
    }
}


export default connect()(App)